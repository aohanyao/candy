package com.td.framework.expand

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet


/**
 * Created by gongziyi on 2018/11/5
 */

/**获取当前约束布局的set集合*/
fun ConstraintLayout.getLayoutSet(): ConstraintSet {
    val set = ConstraintSet()
    set.clone(this)
    return set
}

/**获取当前约束布局的set集合*/
fun ConstraintLayout.setLayoutSet(set: ConstraintSet) {
    setLayoutSet(set, true)
}

/**设置当前约束布局的set集合*/
fun ConstraintLayout.setLayoutSet(set: ConstraintSet, play: Boolean) {
    if (play && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
        android.transition.TransitionManager.beginDelayedTransition(this)
    }
    this.setConstraintSet(set)
}