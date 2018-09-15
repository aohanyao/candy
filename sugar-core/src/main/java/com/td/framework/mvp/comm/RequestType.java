package com.td.framework.mvp.comm;

/**
 * 请求类型
 */
public @interface RequestType {
    int POST = 1;
    int GET = 2;
    int REFRESH = 3;
    int LOAD_MORE = 4;
    int OTHER = 5;
}
