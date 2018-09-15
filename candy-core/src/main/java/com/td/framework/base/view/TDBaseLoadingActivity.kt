package com.td.framework.base.view

import android.os.Build
import android.support.design.widget.CoordinatorLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.td.framework.R
import com.td.framework.utils.statusbar.StatusBarUtil

/**
 * Created by jc on 2016/12/30 0030.
 *
 * Gihub
 *
 * 需要加载数据的Activity
 */

abstract class TDBaseLoadingActivity : TDBaseActivity() {
    protected open var mLoadView: View? = null
    protected open var mRetryView: View? = null
    protected open var mEmptyView: View? = null
    protected open var mNoPermissionsView: View? = null
    protected open var mErrorTextView: TextView? = null
    /**
     * 初始化布局 增加加载框

     * @param resId
     * *
     * @return
     */
    protected fun inflateView(resId: Int): View {
        val contentView = LayoutInflater.from(mActivity).inflate(resId, null, false) as CoordinatorLayout
        initLoadView(contentView)
        return contentView
    }

    /**
     * 初始化
     */
    protected open fun initLoadView(view: View) {
        //其他布局
        mLoadView = LayoutInflater.from(mActivity).inflate(getRootLoadingLayoutId(), null, false)
        mRetryView = LayoutInflater.from(mActivity).inflate(getRootRetryLayoutId(), null, false)
        mErrorTextView = mRetryView?.findViewById(R.id.tvError)
        mEmptyView = LayoutInflater.from(mActivity).inflate(getRootEmptyLayoutId(), null, false)
        mNoPermissionsView = LayoutInflater.from(mActivity).inflate(R.layout.loding_no_permissions, null, false)
        setRetryEvent(mRetryView)
        resetLoadView()

        val frameLayout = FrameLayout(mActivity)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            params.topMargin = (getAppBarLayoutHeight() + StatusBarUtil.getStatusBarHeight(mActivity)).toInt()//为了显示标题栏
        } else {
            params.topMargin = getAppBarLayoutHeight().toInt()//为了显示标题栏
        }
        frameLayout.layoutParams = params

        frameLayout.addView(mLoadView)
        frameLayout.addView(mRetryView)
        frameLayout.addView(mEmptyView)
        frameLayout.addView(mNoPermissionsView)
        (view as CoordinatorLayout).addView(frameLayout)
    }

    /**
     * 返回加载布局ID
     */
    protected open fun getRootLoadingLayoutId(): Int {
        //可以选择在 项目下将loding_base_loading复制过去，进行修改
        return R.layout.loding_base_loading
    }

    /**
     * 返回重试布局
     */
    protected open fun getRootRetryLayoutId(): Int {
        return R.layout.loding_base_retry
    }

    /**
     * 返回空布局
     */
    protected open fun getRootEmptyLayoutId(): Int {
        return R.layout.loding_base_empty
    }

    /**
     * 重置视图
     */
    protected fun resetLoadView() {
        mEmptyView?.visibility = View.GONE
        mRetryView?.visibility = View.GONE
        mLoadView?.visibility = View.GONE
        mNoPermissionsView?.visibility = View.GONE
    }

    /**
     * 显示加载框
     */
    fun showLoading() {
        resetLoadView()
        mLoadView?.visibility = View.VISIBLE
    }

    /**
     * 显示重新尝试的框
     */
    open fun showRetry() {
        resetLoadView();
        mRetryView?.visibility = View.VISIBLE
    }

    /**
     * 设置错误提示的文字
     * @param errorText 错误提示
     */
    protected fun setErrorText(errorText: String?) {
        errorText?.run {
            mErrorTextView?.text = errorText
        }
    }

    /**
     * 无权查看
     */
    fun showNoPermissions() {
        resetLoadView()
        mNoPermissionsView?.visibility = View.VISIBLE
    }

    /**
     * 显示内容
     */
    fun showContent() {
        resetLoadView()
    }

    /**
     * 显示空布局
     */
    fun showEmpty() {
        resetLoadView()
        mEmptyView?.visibility = View.VISIBLE
    }


    /**
     * 重新尝试

     * @param retryView
     */
    protected open fun setRetryEvent(retryView: View?) {
        retryView?.findViewById<View>(R.id.id_retry)?.setOnClickListener {
            showLoading()
            onRetry()
        }
    }

    /**
     * 数据重新尝试
     */
    protected abstract fun onRetry()
}
