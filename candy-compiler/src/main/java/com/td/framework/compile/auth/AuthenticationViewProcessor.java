package com.td.framework.compile.auth;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.td.framework.annotations.auth.AuthenticationView;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * 权限注解处理器
 */
@AutoService(Processor.class)
public class AuthenticationViewProcessor extends AbstractProcessor {
    private Types mTypeUtils;
    private Elements mElementUtils;
    private Filer mFiler;
    private Messager mMessager;

    private static final String SUFFIX = "_AuthView";

    private ClassName mAndroidView;
    private ClassName mAuthHelper;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        //初始化我们需要的基础工具
        mTypeUtils = processingEnv.getTypeUtils();
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();

        // --------------安卓相关的数据
        mAndroidView = ClassName.get("android.view", "View");
        mAuthHelper = ClassName.get("com.td.framework.auth.runtime", "AuthHelper");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        //支持的java版本
        return SourceVersion.RELEASE_7;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //支持的注解
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(AuthenticationView.class.getCanonicalName());
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {//这里开始处理我们的注解解析了，以及生成Java文件


        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AuthenticationView.class);

        if (elements == null || elements.isEmpty()) {
            info("没有找到相关注解");
            return true;
        }
        // 遍历所有被注解了@AuthView的元素
        for (Element annotatedElement : elements) {
            handlerAnnotated(annotatedElement);
        }
        return true;
    }


    /**
     * 处理注解
     *
     * @param classElement
     */
    private void handlerAnnotated(Element classElement) {
        // 获取到注解
        AuthenticationView annotation = classElement.getAnnotation(AuthenticationView.class);
        // 获取到权限code
        String[] authCodes = annotation.authCodes();
        // 获取到View Id
        int[] viewIds = annotation.ids();

        if (authCodes.length != viewIds.length) {
            error(classElement, "ids 与 code必需一一对应。");
            return;
        }

        String newClassName = classElement.getSimpleName().toString() + SUFFIX;


        MethodSpec.Builder startAuthMethod = MethodSpec.methodBuilder("startAuth")
                .addParameter(mAndroidView, "view")
                .addModifiers(Modifier.PUBLIC);

        //遍历所有的view id
        for (int index = 0; index < viewIds.length; index++) {
            int viewId = viewIds[index];
            String code = authCodes[index];

            //添加到方法体中
            startAuthMethod.addStatement("visibleView(view.findViewById($L),$T.checkAuth($S))", viewId
                    , mAuthHelper, code);

        }


        // 显示和隐藏View
        MethodSpec visibleView = MethodSpec.methodBuilder("visibleView")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(mAndroidView, "view")
                .addParameter(Boolean.class, "hasAuth")
                .beginControlFlow("if (null!= view) ")
                .addStatement("view.setVisibility(hasAuth?View.VISIBLE:View.GONE)")
                .endControlFlow()
                .build();

        // 类
        TypeSpec AuthViewCheck = TypeSpec.classBuilder(newClassName)
                .addMethod(startAuthMethod.build())
                .addMethod(visibleView)
                .addModifiers(Modifier.PUBLIC)
                .build();


        PackageElement packageOf = mElementUtils.getPackageOf(classElement);
        try {
            JavaFile.builder(packageOf.getQualifiedName().toString(), AuthViewCheck)
                    .build().writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
            error(classElement, "写出文件错误");
        }

        info("完成注解生成");
    }

    private void error(Element e, String msg, Object... args) {
        mMessager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }

    private void info(String msg, Object... args) {
        mMessager.printMessage(
                Diagnostic.Kind.NOTE,
                String.format(msg, args));
    }


}
