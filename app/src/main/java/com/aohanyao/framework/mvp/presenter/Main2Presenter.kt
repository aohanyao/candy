package com.aohanyao.framework.mvp.presenter

import com.td.framework.model.bean.BaseDataModel
import com.td.framework.mvp.contract.GetContract
import com.td.framework.mvp.presenter.GetDataPresenter
import com.yida.cloud.client.model.bean.Main2Bean
import com.yida.cloud.client.model.param.Main2Param
import io.reactivex.Flowable

/**
 * Created on 2018-3-16 16:31:36
 * @author: jc
 * @version:1.0
 * Description:Main2Activity的Presenter
 */
class Main2Presenter(view: GetContract.View<Main2Bean>)
    : GetDataPresenter<Main2Param, Main2Bean>(view) {
    override fun getGetFlowable(params: Main2Param): Flowable<BaseDataModel<Main2Bean>>? {
        return null
    }
}
