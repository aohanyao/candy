package com.aohanyao.framework

import android.os.Bundle
import com.td.framework.auth.runtime.AuthBind
import com.td.framework.base.view.TDBaseActivity
import com.td.framework.compile.annotations.AuthView
import com.td.framework.utils.StatusBarModeUtil
import kotlinx.android.synthetic.main.activity_main.*

@AuthView(ids = [R.id.btnNextPage, R.id.tvText],
        authCodes = ["A005", "A016"])
class MainActivity : TDBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnNextPage?.setOnClickListener {

        }
        StatusBarModeUtil.StatusBarLightMode(this)

        // 绑定权限
        AuthBind.bind(this)
    }
}
