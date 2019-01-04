package com.td.framework.mvp.comm;

/**
 * 请求类型
 */
public @interface RequestType {
    //--------------------请求类型----------------

    /**
     * 未定义
     */
    public static final int UNDFINE = -991;
    /**
     * 提交数据
     */
    public static final int POST = 1;
    /**
     * 获取数据
     */
    public static final int GET = 2;
    /**
     * 刷新列表
     */
    public static final int REFRESH_LIST = 3;
    /**
     * 加载更多列表
     */
    public static final int LOAD_MORE_LIST = 4;

    //--------------------请求类型----------------
}
