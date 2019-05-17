package com.td.framework.expand

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import android.widget.Toast

fun Context.showText(content: String): Toast {
    val toast = Toast.makeText(this,content,Toast.LENGTH_SHORT)
    toast.show()
    return toast
}

/**获取颜色*/
@ColorInt
fun Context.getColorCompat(@ColorRes res: Int): Int {
    return ContextCompat.getColor(this, res)
}

/**获取颜色*/
@ColorInt
fun Context.getColorCompat(colorString: String): Int {
    return Color.parseColor(colorString)
}

/**获取资源文件*/
fun Context.getDrawableCompat(@DrawableRes res: Int): Drawable? {
    return ContextCompat.getDrawable(this, res)
}

/**dp 转 px @return px*/
fun Context.dp2px(dpValue: Int): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**px 转 dp @return dp*/
fun Context.px2dp(pxValue : Int): Int {
    val scale = resources.displayMetrics.density
    return (pxValue  / scale + 0.5f).toInt()
}