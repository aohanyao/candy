package com.td.framework.ui.adapter;


import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.td.framework.R;

/**
 * 自定义的加载视图
 * <p>
 * --------------------------------------
 * jc update in 2018年10月11日 16:55:17
 * 1. 修改了类名
 * 2. 增加了set方法
 * --------------------------------------
 */

public final class TdListLoadMoreView extends LoadMoreView {
    //------------------------------布局相关
    private int loadingViewId = R.id.load_more_loading_view;
    private int loadFailViewId = R.id.load_more_load_fail_view;
    private int loadEndViewId = R.id.load_more_load_end_view;
    private int layoutId = R.layout.list_view_load_more;
    //------------------------------布局相关

    //------------------------------背景颜色相关
    private int backgroundColor = Color.WHITE;
    //------------------------------背景颜色相关

    @Override
    public int getLayoutId() {
        return layoutId;
    }

    @Override
    protected int getLoadingViewId() {
        return loadingViewId;
    }

    @Override
    protected int getLoadFailViewId() {
        return loadFailViewId;
    }

    @Override
    protected int getLoadEndViewId() {
        return loadEndViewId;
    }

    public void setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

//    public void setLoadingViewId(@IdRes int loadingViewId) {
//        this.loadingViewId = loadingViewId;
//    }
//
//    public void setLoadFailViewId(@IdRes int loadFailViewId) {
//        this.loadFailViewId = loadFailViewId;
//    }
//
//    public void setLoadEndViewId(@IdRes int loadEndViewId) {
//        this.loadEndViewId = loadEndViewId;
//    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        super.convert(holder);
        holder.getView(R.id.load_more_root)
                .setBackgroundColor(backgroundColor);
    }


}
