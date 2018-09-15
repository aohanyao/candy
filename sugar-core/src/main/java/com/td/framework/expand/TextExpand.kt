package com.td.framework.expand

import android.text.Editable
import android.text.InputFilter
import android.view.View
import android.widget.TextView
import com.td.framework.base.listener.BaseTextWatcher
import java.util.regex.Pattern

/**
 * 根据状态(一个文本框绑定多个按钮)
 */
fun TextView.switchLinkViews(length: Int? = 0, vararg views: View) {
    addTextChangedListener(object : BaseTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            views.forEach {
                it.isEnabled = equalLength(length!!)
            }
        }
    })
}

/**
 * 绑定输入
 */
fun TextView.bindClear(clearView: View) {
    addTextChangedListener(object : BaseTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            clearView.visibility = if (equalLength(1)) View.VISIBLE
            else View.INVISIBLE
        }
    })
    clearView.setOnClickListener { reset() }
}

/**
 * 清空数据
 */
fun TextView.reset() {
    text = ""
}


/**
 * 追踪输入
 *@param block 回调当前的输入结果
 */
fun TextView.trackingInput(block: (nowLength: Int) -> Unit) {
    addTextChangedListener(object : BaseTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            block.invoke(contentText().length)
        }
    })
}

/**
 * 返回内容文本
 */
fun TextView.contentText(): String {
    return text.toString()
}

/**
 * 返回内容文本
 * @param defaultText 默认文本
 */
fun TextView.contentText(defaultText: String): String {
    if (isEmptyText()) return defaultText

    return text.toString()
}

/**
 * 判断文本长度
 */
fun TextView.equalLength(length: Int): Boolean {
    return text.toString().length >= length
}

/**
 * 判断是不是空的
 */
fun TextView.isEmptyText(): Boolean {
    return contentText().isNullOrEmpty()
}

/**
 *判断相等
 */
fun TextView.equalsText(text: String?): Boolean {
    return contentText() == (text)
}

/**
 * 字符串更改后
 */
fun TextView.afterTextChanged(block: (str: String) -> Unit) {
    this.addTextChangedListener(object : BaseTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            //返回数据
            block.invoke(contentText())
        }
    })
}

/**
 * 设置像素单位的字体大小
 */
fun TextView.setPxTextSize(px: Float) {
    textSize = px2sp(px)
}

/**
 *  禁止EditText输入特殊字符
 */
fun TextView.setTextInputSpeChat() {
    val filter = InputFilter { source, start, end, dest, dstart, dend ->
        if (source == " " || source.toString().contentEquals("\n")) {
            return@InputFilter ""
        }
        if (dstart > 25 || dest.length > 25) {
            return@InputFilter ""
        }

        val speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
        val pattern = Pattern.compile(speChat)
        val matcher = pattern.matcher(source.toString())
        if (matcher.find()) {
            ""
        } else {
            null
        }
    }
    filters = arrayOf(filter)
}