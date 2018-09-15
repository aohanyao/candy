package com.td.framework.mvp.base

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import com.alibaba.android.arouter.launcher.ARouter
import com.td.framework.base.view.TDBaseLoadingActivity
import com.td.framework.biz.NetError
import com.td.framework.biz.NetError.LOGIN_OUT
import com.td.framework.global.router.RouterBasePath
import com.td.framework.moudle.loding.DialogHelper
import com.td.framework.mvp.comm.RequestType
import com.td.framework.mvp.presenter.BasePresenter


/**
 * Created by jc on 2017/1/6 0006.
 *
 * 版本:1.0.1
 * **说明**<br></br>
 *  * MVP模式下的加载类型Activity
 *
 **** */
abstract class MvpLoadingActivity<out P> : TDBaseLoadingActivity(), DialogInterface.OnCancelListener {

    protected val p: P? by lazy { newP() }

    protected val mDialogHelper: DialogHelper by lazy { DialogHelper(mActivity, this@MvpLoadingActivity) }

    override fun onResume() {
        super.onResume()
        try {
            (p as BasePresenter<*>).subscribe()
        } catch (ignored: Exception) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            (p as BasePresenter<*>).subscribe()
        } catch (ignored: Exception) {
        }

    }


    /**
     * 初始化P层
     */
    protected abstract fun newP(): P?

    /**
     * 失败 信息

     * @param error 错误信息
     */
    protected open fun handlerFail(error: NetError) {

        val message = error.message
        if (message.isNullOrEmpty()) {
            return
        }

        //判断是不是提交数据的类型
        if (error.requestType == RequestType.POST) {
            if (!TextUtils.isEmpty(message)) {
                //提交错误，弹出窗口进行提示
                mDialogHelper.showWarningDialog("提示", message) {}
            }
            return
        }
        //错误类型
        when (error.errorType) {
            NetError.ConnectExceptionError,
            NetError.SocketTimeoutError,
            NetError.UknownError,
            NetError.HttpException -> {
                showRetry()
                if (!TextUtils.isEmpty(message)) {
                    mDialogHelper.showMessageDialog(message)
                }
            }
            NetError.LOGIN_OUT -> {
                showRetry()
                if (!message.isNullOrEmpty()) {
                    mDialogHelper.showMessageDialog(message) {
                        ARouter.getInstance()
                                .build(RouterBasePath.Login)
                                .navigation(mActivity, LOGIN_OUT)
                    }
                }
            }
            NetError.NoDataError -> {
                showEmpty()
            }
            else -> {
                showContent()
                if (!TextUtils.isEmpty(message)) {
                    mDialogHelper.showMessageDialog(message)
                }
            }
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_OUT && resultCode == Activity.RESULT_OK) {
            onRetry()
        }
    }

    /**
     * 完成  弹出信息和关闭弹窗等

     * @param msg 需要显示的信息
     */
    protected open fun handlerComplete(msg: String?) {
        dismissDialog()
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg)
        }
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
                          onDialogConfirmListener: (dialog: AlertDialog) -> Unit = {}) {
        mDialogHelper.showConfirmDialog(message, confirmText, cancelText, onDialogConfirmListener, {})
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
        //显示loading
        showLoadingDialog(mActivity.resources.getString(resId), cancelable)
    }

    /**
     * 显示提示消息

     * @param msg 需要提示的消息
     */
    protected fun showMessageDialog(msg: String) {
        mDialogHelper.showMessageDialog(msg)
    }

    /**
     * 显示提示消息

     * @param msg 需要提示的消息
     */
    protected fun showMessageDialog(msg: String, onDialogConfirmListener: (dialog: AlertDialog) -> Unit = {}) {
        mDialogHelper.showMessageDialog(msg, onDialogConfirmListener)
    }

    override fun onCancel(dialog: DialogInterface) {
        onDialogCancel()
    }

    /**
     * 弹窗 取消
     */
    protected open fun onDialogCancel() {
        //弹窗消失 应该取消请求
        try {
            (p as? BasePresenter<*>?)?.subscribe()
        } catch (ignored: Exception) {
        }

        try {
            subscribe?.dispose()
        } catch (ignored: Exception) {
        }

    }

    /**
     * 显示失败消息

     * @param error
     */
    fun onFail(error: NetError) {
        handlerFail(error)
    }

    /**
     * 完成

     * @param msg
     */
    open fun complete(msg: String) {
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
     * 显示警告框
     */
    fun showWarningDialog(title: Int,
                          strId: Int) {
        //显示弹窗
        mDialogHelper.showWarningDialog(resources.getString(title), resources.getString(strId), {})

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

}
