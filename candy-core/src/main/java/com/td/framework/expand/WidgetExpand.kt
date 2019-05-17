package com.td.framework.expand

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.chad.library.adapter.base.BaseViewHolder
import com.github.promeg.pinyinhelper.Pinyin
import com.td.framework.base.listener.BaseAnimatorListener
import com.td.framework.base.listener.BaseTextWatcher
import com.td.framework.global.app.App
import com.td.framework.utils.DensityUtils
import com.td.framework.utils.KeyBoardUtils
import com.td.framework.utils.SpannableStringUtils
import java.text.DecimalFormat

/**
 * Created by jc on 2017/7/20 0020.
 * <p>版本:1.0.0</p>
 * <b>说明<b><br/>
 * <li>组件拓展</li>
 */


/**
 * 显示或者隐藏
 */
fun View.visibilityOrGone(isVisibility: Boolean) {
    visibility = if (isVisibility) View.VISIBLE
    else View.GONE
}

/**
 * 切换显示状态（多个文本框绑定一个按钮）
 * @param views
 *          first         目标内容长度
 *          second        目标文本
 */
fun View.switchLinkViews(vararg views: Pair<Int, TextView>) {
    //当前View
    val targetView = this
    //填充的数据
    val mEnableViews = HashSet<TextView>()
    views.forEach forEach@{ pair ->
        pair.second.addTextChangedListener(object : BaseTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                views.forEach {
                    //判断长度
                    if (pair.second.equalLength(pair.first)) {
                        mEnableViews.add(pair.second)
                    } else {
                        mEnableViews.remove(pair.second)
                    }
                }
                //相等
                targetView.isEnabled = mEnableViews.size == views.size
            }
        })
    }
}


/**打开键盘*/
fun EditText.openKeyboard() {
    KeyBoardUtils.openKeybord(this, App.newInstance())
}

/**关闭键盘*/
fun EditText.closeKeyboard() {
    KeyBoardUtils.closeKeybord(this, App.newInstance())
}

fun View.alphaAnimator(startAlpha: Float, targetAlpha: Float, duration: Long) {
    //开始动画
    ObjectAnimator.ofFloat(this, "alpha", startAlpha, targetAlpha)
            .setDuration(duration)
            .apply {
                addListener(object : BaseAnimatorListener() {
                    override fun onAnimationStart(animation: Animator?) {
                        if (startAlpha <= 0) {
                            visibility = View.VISIBLE
                        }
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        if (startAlpha > 0) {
                            visibility = View.GONE
                        }
                    }
                })
                start()
            }
}


/**
 * 调整第一个item的顶部边距
 */
fun BaseViewHolder.changeItemTopMargin(context: Context,
                                       rootLayoutId: Int,
                                       marginTop: Float): BaseViewHolder {
    return changeItemTopMargin(context, rootLayoutId, marginTop, 0)
}

/**
 * 设置选中
 */
fun BaseViewHolder.setSelected(viewId: Int, isSelect: Boolean = true): BaseViewHolder {
    getView<View>(viewId).isSelected = isSelect
    return this
}

/**
 * 调整item的顶部边距
 */
fun BaseViewHolder.changeItemTopMargin(context: Context,
                                       rootLayoutId: Int,
                                       marginTop: Float,
                                       vararg position: Int): BaseViewHolder {
    val rootView = getView<View>(rootLayoutId)
    val layoutParams = rootView.layoutParams as? ViewGroup.MarginLayoutParams?
    layoutParams?.apply {

        topMargin = if (layoutPosition in position) {
            DensityUtils.dp2px(context, marginTop)
        } else {
            0
        }

        rootView.layoutParams = layoutParams
    }
    return this
}


/**
 * 转换参数
 */
fun View.px2sp(px: Float): Float {
    return DensityUtils.px2sp(context, px)
}


/**
 * 获取颜色
 */
fun Activity.getResourceColor(color: Int): Int {
    return resources.getColor(color)
}

fun Fragment.getResourceColor(color: Int): Int {
    return resources.getColor(color)
}

fun AppCompatEditText.switchEnterType(block: (isPassword: Boolean) -> Unit) {
    val type = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    if (inputType === type) {
        inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        setSelection(text?.length?:0)     //把光标设置到当前文本末尾
        block.invoke(true)
    } else {
        inputType = type
        setSelection(text?.length?:0)
        block.invoke(false)

    }
}

/**
 * 设置特别的文字价格
 */
fun TextView.setUnusualPrices(strPrice: String?, vararg other: String) {

    text = try {
        val mStringPrice = strPrice?.split(".")
        val mPriceText = SpannableStringUtils.getBuilder("￥")
                .append(mStringPrice?.get(0) ?: "0")
//                .setBold()
                .setProportion(1.5f)
                .append(".${mStringPrice?.get(1)}")

        other.forEach {
            mPriceText.append(it)
                    .setForegroundColor(0xff666666.toInt())
        }

        // 设置价格
        mPriceText.create()
    } catch (e: Exception) {
        strPrice
    }

}

/**
 * 转换为千分位的人民币
 */
fun Double.toRMB(): String {
    return try {
        //补位
        "￥" + DecimalFormat("###,##0.00").format(this)
    } catch (e: Exception) {
        "￥0.00"
    }
}

/**查询字符串首字母*/
fun String.toPinYinFirst(): String {
    if (this.isNotEmpty()) {
        val charArray = this.toCharArray()
        var firstString = ""
        for (c in charArray) {
            firstString += Pinyin.toPinyin(c).get(0).toUpperCase()
        }
        return firstString
    }
    return ""
}

/**封装字符串搜索高亮*/
fun String.searchToSpan(searchKey: String, @ColorInt themeColor: Int): SpannableString? {
    val spannableString = SpannableString(this)
    if (searchKey.isEmpty())
        return null

    if (contains(searchKey)) {
        /**完整匹配*/
        val start = indexOf(searchKey)
        spannableString.setSpan(ForegroundColorSpan(themeColor), start, start + searchKey.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }
    /**首拼匹配*/
    val keyPinYin = searchKey.toPinYinFirst()
    val messagePinyin = this.toPinYinFirst()
    if (messagePinyin.contains(keyPinYin)) {
        val start = messagePinyin.indexOf(keyPinYin)
        spannableString.setSpan(ForegroundColorSpan(themeColor), start, start + keyPinYin.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    /**全拼*/
    val keyPy = Pinyin.toPinyin(searchKey, "%@%")
    val msgPy = Pinyin.toPinyin(this, "%@%")
    if (msgPy.contains(keyPy)) {
        val keysplit = keyPy.split("%@%")
        val msgsplit = msgPy.split("%@%")
        var index = -1
        for (i in 0..msgsplit.size - 1) {
            if (msgsplit[i].equals(keysplit[0])) {
                index = i
                break
            }
        }
        if (index != -1) {
            spannableString.setSpan(ForegroundColorSpan(themeColor), index, index + keysplit.size, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannableString
        }
    }
    return null
}
