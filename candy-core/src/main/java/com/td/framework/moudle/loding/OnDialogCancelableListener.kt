package com.td.framework.moudle.loding

import android.content.DialogInterface

/**
 * Created by jc on 2018-05-19.
 * Version:1.0
 * Description:弹窗取消
 * ChangeLog:
 */
interface OnDialogCancelableListener {
    fun onCancel(dialog: DialogInterface)
}