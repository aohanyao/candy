package com.td.framework.mvp.base

import android.content.DialogInterface
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import com.alibaba.android.arouter.launcher.ARouter
import com.td.framework.base.view.TDBaseActivity
import com.td.framework.biz.NetError
import com.td.framework.global.router.RouterBasePath
import com.td.framework.moudle.loding.DialogHelper
import com.td.framework.mvp.presenter.BasePresenter


/**
 * Created by jc on 2017/1/6 0006.
 *
 * Github
 */

abstract class MvpBaseActivity<P> : TDBaseActivity(), DialogInterface.OnCancelListener {
    protected val p: P? by lazy { newP() }

    protected val mDialogHelper: DialogHelper by lazy {
        DialogHelper(mActivity, this)
    }


    public override fun onResume() {
        super.onResume()
        if (p != null) {
            if (checkIsPresenter())
                (p as BasePresenter<*>).subscribe()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (p != null) {
            if (checkIsPresenter())
                (p as BasePresenter<*>).unSubscribe()
        }
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
    protected fun handlerFail(error: NetError) {
        //判断是不是提交数据的类型

        val message = error.message
        if (TextUtils.isEmpty(message)) {
            return
        }
        if (error.errorType == NetError.LOGIN_OUT || message!!.contains("登陆失效")) {
            if (!TextUtils.isEmpty(message)) {
                mDialogHelper.showMessageDialog(message) {
                    ARouter.getInstance()
                            .build(RouterBasePath.Login)
                            .navigation()
                    finish()
                }
            }
        } else {
            if (!TextUtils.isEmpty(message)) {

                mDialogHelper.showMessageDialog(message)
            }
        }
    }

    /**
     * 完成  弹出信息和关闭弹窗等

     * @param msg
     */
    protected fun handlerComplete(msg: String) {
        dismissDialog()
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg)
        }
    }

    /**
     * 关闭弹窗
     */
    protected fun dismissDialog() {
        mDialogHelper.dismissDialog()
    }

    /**
     * 显示loading弹窗

     * @param msg        消息
     * *
     * @param cancelable 是否可以取消
     */
    protected fun showLoadingDialog(msg: String, cancelable: Boolean) {
        mDialogHelper.showLoadingDialog(msg, cancelable)
    }

    /**
     * 显示loading弹窗

     * @param resId      消息
     * *
     * @param cancelable 是否可以取消
     */
    protected fun showLoadingDialog(@StringRes resId: Int, cancelable: Boolean) {
        dismissDialog()
        //显示loading
        showLoadingDialog(mActivity.resources.getString(resId), cancelable)
    }

    /**
     * 显示提示消息

     * @param msg
     */
    protected fun showMessageDialog(msg: String) {
        mDialogHelper.showMessageDialog(msg)
    }

    /**
     * 显示提示消息

     * @param msg
     */
    protected fun showMessageDialog(msg: String, onDialogConfirmListener: (dialog: AlertDialog) -> Unit = {}) {
        mDialogHelper.showMessageDialog(msg, onDialogConfirmListener)
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


    override fun onCancel(dialog: DialogInterface) {
        onDialogCancel()
    }

    /**
     * 弹窗 取消
     */
    protected open fun onDialogCancel() {
        //弹窗消失 应该取消请求
        if (p != null) {
            if (checkIsPresenter()) {
                (p as BasePresenter<*>).unSubscribe()
            }
        }
        subscribe?.dispose()
    }

    /**
     * 检查是不是presenter

     * @return
     */
    private fun checkIsPresenter(): Boolean {

        if (p !is BasePresenter<*>) {
            throw RuntimeException("your P request extend BasePresenter")
        }

        return true
    }

    /**
     * 显示确认信息：标题 内容 按钮文字等

     * @param message                 信息
     * *
     * @param confirmText             确认按钮文字
     * *
     * @param onDialogConfirmListener 确认按钮点击
     */
    fun showConfirmDialog(message: String?,
                          confirmText: String?,
                          cancelText: String?,
                          onDialogConfirmListener: (dialog: AlertDialog) -> Unit = {}, onDialogCancelableListener: (dialog: AlertDialog) -> Unit = {}) {
        mDialogHelper.showConfirmDialog(message, confirmText, cancelText, onDialogConfirmListener, onDialogCancelableListener)
    }

    fun showConfirmDialog(message: String?,
                          confirmText: String?,
                          cancelText: String?,
                          onDialogConfirmListener: (dialog: AlertDialog) -> Unit = {}) {
        mDialogHelper.showConfirmDialog(message, confirmText, cancelText, onDialogConfirmListener, {})
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
        dismissDialog()
        showLoadingDialog(resources.getString(msg), true)
    }
}
