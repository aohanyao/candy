package com.td.framework.annotations.auth;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于认证的注解，进行权限的判断
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AuthenticationView {
    /**
     * 需要进行权限验证的View
     */
    @IdRes int[] ids();

    /**
     * 认证编码
     *
     * @return
     */
    String[] authCodes();
}
