package com.td.framework.auth.runtime;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 江俊超 on 2018/10/12.
 * Version:1.0
 * Description:
 * ChangeLog:
 */
public class AuthHelper {
    private static Set<String> testAuth;

    /**
     * 检查权限,当前是测试的，
     *
     * @param authCode
     * @return
     */
    public synchronized static boolean checkAuth(String authCode) {
        if (null == testAuth) {
            testAuth = new HashSet<>();
            for (int i = 0; i < 10; i++) {
                testAuth.add("A00" + i);
            }
        }

        return testAuth.contains(authCode);
    }
}
