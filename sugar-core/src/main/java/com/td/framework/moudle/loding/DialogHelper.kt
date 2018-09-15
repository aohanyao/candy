package com.td.framework.moudle.loding

import android.app.Activity
import android.content.DialogInterface
import android.support.annotation.StyleRes
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.td.framework.R

/**
 * Created by jc on 2017/1/17 0017.
 *-----------------------------------------------
 * update by jc on 2018年5月19日 14:47:48
 * 根据此项目，进行了弹窗的新增
 *-----------------------------------------------
 * 弹窗帮助类
 */
class DialogHelper private constructor(private val mActivity: Activity, private val onCancelListener: DialogInterface.OnCancelListener, @param:StyleRes private val mStyle: Int) {
    /**布局解析器*/
    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(mActivity) }
    private lateinit var mDialog: AlertDialog

    constructor(mActivity: Activity, onCancelListener: DialogInterface.OnCancelListener) : this(mActivity, onCancelListener, R.style.AppAlertDialogStyle) {}


    /**
     * 显示loading弹窗
     *
     * @param msg        消息
     * @param cancelable 是否可以取消
     */
    fun showLoadingDialog(msg: String, cancelable: Boolean) {
        dismissDialog()
        //解析布局
        mInflater.inflate(R.layout.dialog_loading, null)?.apply {
            this.findViewById<TextView>(R.id.tv_loading_text)?.text = msg
            //创建和显示弹窗
            createAndShowDialog(mContentView = this, cancelable = cancelable)
        }
    }

    /**
     * 显示消息的弹窗
     *
     * @param message
     */
    fun showMessageDialog(message: String?,
                          onConfirm: (dialog: AlertDialog) -> Unit = {}) {
        dismissDialog()
        //解析布局
        mInflater.inflate(R.layout.dialog_message, null)?.apply {
            //消息
            this.findViewById<TextView>(R.id.tv_dialog_message)?.text = message ?: ""
            //确认按钮
            initActionButton(this.findViewById(R.id.btn_confirm),
                    "确定", onConfirm)

            //创建和显示弹窗
            createAndShowDialog(this)
        }
    }


    /**
     * 显示成功窗口
     */
    fun showSuccessDialog(title: String?,
                          message: String?,
                          onConfirm: (dialog: AlertDialog) -> Unit?) {
        //显示弹窗
        showHasIconDialog(R.mipmap.icon_dialgo_complete, title, message, onConfirm)
    }


    /**
     * 显示警告框
     */
    fun showWarningDialog(title: String?,
                          message: String?,
                          onConfirm: (dialog: AlertDialog) -> Unit?) {
        //显示弹窗
        showHasIconDialog(R.mipmap.icon_dialgo_warning, title, message, onConfirm)

    }

    /**
     * 显示提示框
     */
    fun showTipDialog(title: String?,
                      message: String?,
                      onConfirm: (dialog: AlertDialog) -> Unit?) {
        //显示弹窗
        showHasIconDialog(R.mipmap.icon_dialgo_notice, title, message, onConfirm)
    }

    /**
     * 显示有图标的弹窗
     */
    private fun showHasIconDialog(icon: Int,
                                  title: String?,
                                  message: String?,
                                  onConfirm: (dialog: AlertDialog) -> Unit?) {
        //解析布局
        mInflater.inflate(R.layout.dialog_has_tip_message, null)?.apply {
            //顶部图标
            this.findViewById<ImageView>(R.id.iv_dialog_icon)?.setImageResource(icon)
            //标题
            this.findViewById<TextView>(R.id.tv_dialog_title)?.text = title
            //消息
            this.findViewById<TextView>(R.id.tv_dialog_message)?.text = message
            //确认按钮
            initActionButton(this.findViewById(R.id.btn_confirm), "确定",
                    onConfirm)
            //创建并显示
            createAndShowDialog(this, false)
        }
    }

    fun showConfirmDialog(message: String?,
                          onConfirm: (dialog: AlertDialog) -> Unit?,
                          onCancel: (dialog: AlertDialog) -> Unit?) {
        //显示弹窗
        showConfirmDialog(message, "确认", "取消", onConfirm, onCancel)
    }


    /**
     * 显示确认信息：标题 内容 按钮文字等
     *
     * @param message                    信息
     * @param confirmText                确认按钮文字
     * @param cancelText                 取消按钮文字
     * @param onConfirm    确认按钮点击
     * @param onDialogCancelableListener 取消按钮点击
     */
    fun showConfirmDialog(message: String?,
                          confirmText: String?,
                          cancelText: String?,
                          onConfirm: (dialog: AlertDialog) -> Unit?,
                          onDialogCancelableListener: (dialog: AlertDialog) -> Unit?) {
        dismissDialog()
        //解析布局
        mInflater.inflate(R.layout.dialog_confirm, null)?.apply {
            //消息
            this.findViewById<TextView>(R.id.tv_dialog_message)?.text = message
            //确认按钮
            initActionButton(this.findViewById(R.id.btn_confirm),
                    confirmText ?: "确定",
                    onConfirm)

            //取消按钮
            initActionButton(this.findViewById(R.id.btn_cancel),
                    cancelText ?: "取消",
                    onDialogCancelableListener)
            //创建和显示弹窗
            createAndShowDialog(this)
        }
    }


    /**创建和显示弹窗*/
    private fun createAndShowDialog(mContentView: View, cancelable: Boolean = false) {
        //创建弹窗
        mDialog = AlertDialog.Builder(mActivity, mStyle)
                .setView(mContentView)
                .setCancelable(cancelable)
                .setOnCancelListener(onCancelListener)
                .create()
        try {
            mDialog.show()
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }


    /**初始化点击按钮
     * @param button 需要设置的按钮
     * @param showText 显示的文字
     * @param callback 相应点击的回调
     */
    private fun initActionButton(button: Button?, showText: String?, callback: (dialog: AlertDialog) -> Unit?) {
        button?.apply {
            text = showText ?: "取消"
            setOnClickListener {
                dismissDialog()
                callback.invoke(mDialog)
            }
        }
    }

    /**
     * 关闭弹窗
     */
    fun dismissDialog() {
        try {
            onCancelListener.onCancel(mDialog)
            mDialog.dismiss()
        } catch (e: Exception) {
            //e.printStackTrace();
        }

    }


}