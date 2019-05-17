package com.td.framework.base.view

import android.os.Bundle
import com.td.framework.biz.NetError


/**
 * Created by Administrator on 2016/12/14 0014.
 *
 * 基类 活动
 *
 * 没有滑动返回的
 */

open class NoSwipeBaseActivity : TDBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSwipeBackEnable(false)
    }


    open fun onFail(error: NetError) {
        complete(error.message ?: "")
    }

    open fun complete(msg: String) {
        msg.let { showToast(msg) }
    }

    open fun showLoading(msg: Int) {
    }


}
