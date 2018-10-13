package com.td.framework.auth.runtime;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 江俊超 on 2018/10/12.
 * Version:1.0
 * Description: 检查权限的接口
 * ChangeLog:
 */
public class AuthBind {

    /**
     * 末尾拼接
     */
    private static final String SUFFIX = "_AuthView";

    /**
     * 缓存
     */
    private static final Map<String, Object> CACHE = new LinkedHashMap<>();

    private AuthBind() {

    }

    /**
     * 在Activity中进行绑定
     * <p>
     * 请在setContentView之后进行调用
     *
     * @param activity activity
     * @return
     */
    public synchronized static AuthBind bind(Activity activity) {
        //主布局
        View contentView = activity.getWindow().getDecorView();


        return bind(contentView, activity.getLocalClassName());
    }

    /**
     * 绑定 android.support.v4.app.Fragment，
     * 请在 【super.onViewCreated(view, savedInstanceState)】之后调用
     *
     * @param fragment
     * @return
     */
    public synchronized static AuthBind bind(Fragment fragment) {
        //主布局
        View contentView = fragment.getView();
        return bind(contentView, fragment.getClass().getName());
    }

    /**
     * 通过反射，获取到对应的数据
     *
     * @param contentView view
     * @param packageName 包名
     * @return
     */
    private static AuthBind bind(View contentView, String packageName) {

        String refName = packageName + "" + SUFFIX;

        try {
            Class<?> clazz = Class.forName(refName);
            Method method = clazz.getMethod("startAuth", View.class);
            method.invoke(clazz.newInstance(), contentView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AuthBind();
    }

}
