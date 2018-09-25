package com.td.framework.mvp.base

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import com.td.framework.biz.NetError
import com.td.framework.mvp.presenter.BasePresenter

/**
 * Created by jc on 2017/1/9 0009.
 *
 * Gihub
 *  * 有loading的
 *
 */
abstract class MvpBaseLoadingFragment<P> : MvpBaseFragment<P>(){

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



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NetError.LOGIN_OUT && resultCode == Activity.RESULT_OK) {
            onRetry()
        }
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

    override fun onCancel(dialog: DialogInterface) {
        onDialogCancel()
    }

    /**
     * 弹窗 取消
     */
    protected fun onDialogCancel() {
        //弹窗消失 应该取消请求
        (p as? BasePresenter<*>?)?.unSubscribe()
    }




}
