package com.td.framework.mvp.base

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.td.framework.biz.NetError
import com.td.framework.global.BasicAdapterHelper
import com.td.framework.global.app.Constant
import com.td.framework.mvp.comm.RequestType
import com.td.framework.mvp.contract.GeneralLoadDataContract
import com.td.framework.mvp.model.BaseParamsInfo
import com.td.framework.mvp.model.EmptyParamsInfo
import com.td.framework.ui.adapter.CustomLoadMoreView
import com.td.framework.ui.refresh.RefreshLayout
import java.util.*

/**
 * Created by jc on 7/26/2017.
 *
 * 版本:1.0.0
 * **说明**<br></br>
 *  * 专门用来加载数据列表的基类
 * @param P P层
 * *
 * @param T 数据对象
 **** */
abstract class MvpLoadListDataBaseFragment<P : GeneralLoadDataContract.GeneralLoadDataPresenter<*, *, *>, T>
    : MvpBaseLoadingFragment<P>(), GeneralLoadDataContract.GeneralLoadDataView<T>, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    //数据对象
    protected var mDatas: MutableList<T> = ArrayList()
    //适配器
    protected val mAdapter: BaseQuickAdapter<T, *>? by lazy {
        getAdapter()
    }
    //参数对象
    protected val mParam: BaseParamsInfo by lazy { getParam() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflateView(getLayoutId(), container)
        showLoading()
        return view
    }

    protected open fun getParam(): BaseParamsInfo {
        return EmptyParamsInfo()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        if (!isCreate) {
//            p?.refreshData(mParam)
            onRefresh()
        }
    }


    /**
     * 初始化适配器
     */
    protected open fun initAdapter() {
        //适配器
        mAdapter?.apply {
            setLoadMoreView(getLoadMoreView())
            setOnLoadMoreListener(this@MvpLoadListDataBaseFragment)
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
            }
        })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isCreate) {
            isCreate = false
//            p?.refreshData(mParam)
            onRefresh()
        }
    }

    override fun onRetry() {
        p?.refreshData(mParam)
    }


    override fun handlerFail(error: NetError) {
        //判断是不是加载更多
        if (error.requestType == RequestType.LOAD_MORE) {
            //加载失败
            mAdapter?.loadMoreFail()
        } else {
            super.handlerFail(error)
            (getSwipeRefreshLayout() as? RefreshLayout?)?.isRefreshing = false
        }
    }

    override fun refreshSuccess(datas: List<T>) {
        mDatas = datas as MutableList<T>
        mAdapter?.setNewData(mDatas)
        (getSwipeRefreshLayout() as? SwipeRefreshLayout?)?.isRefreshing = false
        showContent()
    }

    override fun commitDataSuccess() {
        //提交数据成功
    }

    override fun commitDataFail() {
        //提交数据失败
    }

    override fun loadMoreSuccess(datas: List<T>) {
        mDatas.addAll(datas)
        mAdapter?.notifyDataSetChanged()
        mAdapter?.loadMoreComplete()
    }

    protected open fun refresh() {
        p?.refreshData(mParam)
    }

    /**
     * 打开和关闭底布局
     *
     * 当前数据集合小于页大小
     */
    protected fun goneLoadMoreEnd() {
        if (mDatas.size < Constant.PAGE_SIZE)
            mAdapter?.loadMoreEnd(true)
    }

    override fun noMore() {
        mAdapter?.loadMoreEnd()
    }

    override fun onLoadMoreRequested() {
        p?.loadMoreData(mParam)
    }


    /**
     * 返回加载布局
     */
    protected open fun getLoadMoreView(): LoadMoreView {
        return CustomLoadMoreView()
    }

    /**
     * 获取刷新布局
     * * 写View的原因是为了方便以后假如替换相关的刷新
     */
    protected open fun getSwipeRefreshLayout(): View? {
        return null
    }

    /**
     * 获取适配器
     */
    protected abstract fun getAdapter(): BaseQuickAdapter<T, *>?

    /**
     * 获取RecyclerView
     */
    protected abstract fun getRecyclerView(): RecyclerView?

    /**
     * 点击事件

     * @param adapter  适配器
     * *
     * @param view     点击的View
     * *
     * @param position 下标
     * *
     * @param data     点击的数据
     */
    protected abstract fun onRecyclerViewItemChildClick(view: View, position: Int, data: T)

    /**
     * 获取布局ID
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 获取内容为空的提示

     * @return
     */
    protected open fun getEmptyTip(): String {
        return ""
    }

    /**
     * 设置是否模拟
     */
    protected open fun getMock(): Boolean {
        return false
    }

    override fun onRefresh() {
        if (getMock()) {
            (getSwipeRefreshLayout() as? SwipeRefreshLayout?)?.isRefreshing = false
        } else {
            p?.refreshData(mParam)
        }
    }
}