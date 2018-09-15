package com.td.framework.mvp.base

import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.text.TextUtils

import com.alibaba.android.arouter.launcher.ARouter
import com.td.framework.base.view.TDBaseLoadingFragment
import com.td.framework.biz.NetError
import com.td.framework.global.router.RouterBasePath
import com.td.framework.moudle.loding.DialogHelper
import com.td.framework.mvp.comm.RequestType
import com.td.framework.mvp.presenter.BasePresenter

/**
 * Created by jc on 2017/1/9 0009.
 *
 * Gihub
 *
 * MVPFramgnrt
 */
abstract class MvpBaseFragment<P> : TDBaseLoadingFragment(),
        DialogInterface.OnCancelListener{
    protected var p: P? = null
    protected val mDialogHelper: DialogHelper by lazy {
        DialogHelper(mActivity, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p = getPer()
    }

    override fun onResume() {
        super.onResume()
        try {
            (p as BasePresenter<*>).subscribe()
        } catch (ignored: Exception) {
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            (p as BasePresenter<*>).subscribe()
        } catch (ignored: Exception) {
        }

    }

    override fun onRetry() {

    }

    protected fun getPer(): P? {
        if (p == null) {
            p = newP()
        }
        return p
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 初始化P层

     * @return
     */
    protected abstract fun newP(): P?

    /**
     * 失败 信息

     * @param error
     */
    protected open fun handlerFail(error: NetError) {
        val message = error.message
        if (TextUtils.isEmpty(message)) {
            return
        }

        dismissDialog()
        handlerComplete(null)
        //判断是不是提交数据的类型
        if (error.requestType == RequestType.POST) {
            if (!TextUtils.isEmpty(message)) {
                //提交错误，弹出窗口进行提示
                mDialogHelper.showWarningDialog("提示", message) {}
            }
            return
        }
        if (error.errorType == NetError.ConnectExceptionError ||
                error.errorType == NetError.SocketTimeoutError ||
                error.errorType == NetError.HttpException) {
            showRetry()
        } else if (error.errorType == NetError.LOGIN_OUT || message!!.contains("登陆失效")) {
            showRetry()

            if (!TextUtils.isEmpty(message)) {
                mDialogHelper.showMessageDialog(message) {
                    ARouter.getInstance()
                            .build(RouterBasePath.Login)
                            .navigation(mActivity, NetError.LOGIN_OUT)
                }
            }
        } else {
            showContent()
            if (!TextUtils.isEmpty(message)) {
                mDialogHelper.showMessageDialog(message)
            }
        }
    }
    /**
     * 显示成功窗口
     */
    fun showSuccessDialog(title: String?,
                          message: String?,
                          onConfirm: (dialog: AlertDialog) -> Unit?) {
        //显示弹窗
        mDialogHelper.showSuccessDialog(title, message, onConfirm)
    }

    /**
     * 显示成功窗口
     */
    fun showSuccessDialog(title: String?,
                          message: String?) {
        //显示弹窗
        mDialogHelper.showSuccessDialog(title, message, {})
    }


    /**
     * 显示警告框
     */
    fun showWarningDialog(title: String?,
                          message: String?,
                          onConfirm: (dialog: AlertDialog) -> Unit?) {
        //显示弹窗
        mDialogHelper.showWarningDialog(title, message, onConfirm)

    }

    /**
     * 显示警告框
     */
    fun showWarningDialog(title: String?,
                          message: String?) {
        //显示弹窗
        mDialogHelper.showWarningDialog(title, message, {})

    }

    /**
     * 显示提示框
     */
    fun showTipDialog(title: String?,
                      message: String?,
                      onConfirm: (dialog: AlertDialog) -> Unit?) {
        //显示弹窗
        mDialogHelper.showTipDialog(title, message, onConfirm)
    }

    /**
     * 显示提示框
     */
    fun showTipDialog(title: String?,
                      message: String?) {
        //显示弹窗
        mDialogHelper.showTipDialog(title, message, {})
    }
    /**
     * 完成  弹出信息和关闭弹窗等

     * @param msg
     */
    protected fun handlerComplete(msg: String?) {
        dismissDialog()
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg!!)
        }
    }

    /**
     * 关闭弹窗
     */
    protected fun dismissDialog() {
        mDialogHelper.dismissDialog()
    }

    fun onFail(error: NetError) {
        handlerFail(error)
    }

    fun complete(msg: String) {
        handlerComplete(msg)
    }

    /**
     * 显示弹窗

     * @param msg
     */
    fun showLoading(@StringRes msg: Int) {
        mDialogHelper.dismissDialog()
        mDialogHelper.showLoadingDialog(resources.getString(msg), true)
    }

    override fun onCancel(dialog: DialogInterface) {
        if (subscribe != null) {
            subscribe!!.dispose()
            subscribe = null
        }
    }


}
