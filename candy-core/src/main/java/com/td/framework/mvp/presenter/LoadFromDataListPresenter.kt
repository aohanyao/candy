package com.td.framework.mvp.presenter

import com.td.framework.mvp.contract.GeneralLoadDataContract
import com.td.framework.mvp.model.BaseParamsInfo

/**
 * Created by gongziyi on 12/25/2018.
 * <p>版本:1.0.0</p>
 * <b>说明<b><br/>
 *  * 加载数据列表的P
 *  *
 *  这样单独写一个出来的原因是为了少些一些代码
 *
 *
 *
 */
abstract class LoadFromDataListPresenter<T, out RP:BaseParamsInfo>(view: GeneralLoadDataContract.GeneralLoadDataView<T>)
    : GeneralLoadDataContract.GeneralFormDataPresenter<GeneralLoadDataContract.GeneralLoadDataView<T>, T,RP>(view)