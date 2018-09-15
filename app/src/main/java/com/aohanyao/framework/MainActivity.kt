package com.aohanyao.framework

import android.os.Bundle
import com.td.framework.base.view.TDBaseActivity
import com.td.framework.utils.StatusBarModeUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : TDBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnNextPage?.setOnClickListener {   }
        StatusBarModeUtil.StatusBarLightMode(this)
    }
}
