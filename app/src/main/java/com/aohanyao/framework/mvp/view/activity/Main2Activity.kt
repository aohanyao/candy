package com.aohanyao.framework.mvp.view.activity

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.aohanyao.framework.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.td.framework.mvp.base.MvpLoadListDataBaseActivity
import kotlinx.android.synthetic.main.content_main2.*
import com.aohanyao.framework.mvp.presenter.Main2Presenter
import com.td.framework.mvp.base.MvpBaseActivity
import com.td.framework.mvp.contract.GetContract
import com.yida.cloud.client.model.bean.Main2Bean
import com.yida.cloud.client.model.param.Main2Param

/**
 * Created on 2018-3-16 16:31:36
 * @author: jc
 * @version:1.0
 * Description:Main2ActivityActivity
 */
class Main2Activity : MvpBaseActivity<Main2Presenter>(), GetContract.View<Main2Bean> {


    /** 参数对象*/
    private val mParam by lazy {
        Main2Param()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
    }

    override fun getSuccess(data: Main2Bean) {
    }

    override fun getFail(msg: String?) {
    }

    override fun newP(): Main2Presenter? {
        return Main2Presenter(this)
    }
}
