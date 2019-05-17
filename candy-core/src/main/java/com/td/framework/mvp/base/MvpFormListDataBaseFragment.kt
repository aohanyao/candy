package com.td.framework.mvp.base

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.td.framework.global.BasicAdapterHelper
import com.td.framework.mvp.contract.GeneralLoadDataContract

/**
 * Created on 2018/12/25
 * @author: gongziyi
 * @version: 1.0
 * Description:
 * ChangeLog:
 */
abstract class MvpFormListDataBaseFragment<P : GeneralLoadDataContract.GeneralFormDataPresenter<*, *, *>, T>
    : MvpLoadListDataBaseFragment<P, T>() {

    /**
     * 初始化适配器
     */
    protected open override fun initAdapter() {
        //适配器
        mAdapter?.apply {
            BasicAdapterHelper.initAdapterVertical(mActivity, mAdapter, getRecyclerView(), getEmptyTip(), getListEmptyLayoutId())
            getRecyclerView()?.adapter = this
        }
        //刷新
        (getSwipeRefreshLayout() as? SwipeRefreshLayout?)?.setOnRefreshListener(this)
        //点击
        getRecyclerView()?.addOnItemTouchListener(object : OnItemChildClickListener() {
            override fun onSimpleItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                //回调
                onRecyclerViewItemChildClick(view, position, mDatas[position])
                onRecyclerViewItemChildClick(adapter, view, position, mDatas[position])
            }
        })
    }
}