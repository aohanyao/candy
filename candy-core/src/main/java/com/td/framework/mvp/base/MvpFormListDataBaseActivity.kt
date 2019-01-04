package com.td.framework.mvp.base

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.td.framework.global.BasicAdapterHelper
import com.td.framework.mvp.contract.GeneralLoadDataContract
import com.td.framework.ui.refresh.RefreshLayout

/**
 * Created on 2018/12/25
 * @author: gongziyi
 * @version: 1.0
 * Description:
 * ChangeLog:
 */
abstract class MvpFormListDataBaseActivity<P : GeneralLoadDataContract.GeneralFormDataPresenter<*, *, *>, T>
    : MvpLoadListDataBaseActivity<P, T>() {

    /**
     * 初始化适配器
     */
    override open fun initAdapter() {
        //适配器
        mAdapter?.apply {
            BasicAdapterHelper.initAdapter(mActivity, mAdapter,
                    getRecyclerView(), getRecyclerViewOrientation(),
                    getEmptyTip(), getListEmptyLayoutId())
            getRecyclerView()?.adapter = this
            getHeaderView()?.apply {
                addHeaderView(this)
            }
            setHeaderAndEmpty(true)
        }
        //刷新
        (getSwipeRefreshLayout() as? RefreshLayout?)?.setOnRefreshListener(this)
        //点击
        getRecyclerView()?.addOnItemTouchListener(object : OnItemChildClickListener() {
            override fun onSimpleItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                //回调
                onRecyclerViewItemChildClick(view, position, mDatas[position])
            }
        })
    }
}