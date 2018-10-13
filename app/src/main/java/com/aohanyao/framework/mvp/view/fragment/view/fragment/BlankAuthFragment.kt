package com.aohanyao.framework.mvp.view.fragment.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aohanyao.framework.R
import com.td.framework.auth.runtime.AuthBind
import com.td.framework.base.view.TDBaseFragment
import com.td.framework.compile.annotations.AuthView

/**
 * Created on 2018-10-13 9:51:15
 * @author: jc
 * @version:1.0
 * Description:测试权限的Fragment
 */
@AuthView(ids = [R.id.mA006, R.id.mA017], authCodes = ["A006", "A017"])
class BlankAuthFragment : TDBaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_blank_auth, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AuthBind.bind(this)
    }

    companion object {
        /**
         * 获取BlankFragment的实例
         */
        fun newInstance(): BlankAuthFragment {
            val args = Bundle()
            val fragment = BlankAuthFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
