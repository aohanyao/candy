package com.td.framework.auth.runtime;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Method;

/**
 * Created by 江俊超 on 2018/10/12.
 * Version:1.0
 * Description: 检查权限的接口
 * ChangeLog:
 */
public class AuthBind {

    private static final String SUFFIX = "_AuthView";


    private AuthBind() {

    }

    /**
     * 在Activity中进行绑定
     *
     * @param activity
     * @return
     */
    public static AuthBind bind(Activity activity) {
        //主布局
        View contentView = activity.getWindow().getDecorView();


        return bind(contentView, activity);
    }

    /**
     * 通过反射，获取到对应的数据
     *
     * @param contentView
     * @param activity
     * @return
     */
    private static AuthBind bind(View contentView, Activity activity) {
        // 开始反射
        String packageName = activity.getLocalClassName();

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
