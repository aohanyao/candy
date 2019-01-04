package ${packageName}.presenter

import com.td.framework.model.bean.FormListDataModel
import com.td.framework.mvp.contract.GeneralLoadDataContract
import com.td.framework.mvp.presenter.LoadFromDataListPresenter
import ${ParamPackageName}.${ParamClass}
import ${BeanPackageName}.${BeanClass}
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

/**
 * Created on ${.now}
 * @author: gongziyi
 * @version:1.0
 * Description:${activityTitle}的Presenter
 */
class  ${presenterClass}(view: GeneralLoadDataContract.GeneralLoadDataView<${BeanClass}>)
    : LoadFromDataListPresenter<${BeanClass}, ${ParamClass}>(view) {


    override fun getRequestFormDataObservable(params: ${ParamClass}): Flowable<FormListDataModel<${BeanClass}>>? {
          //-----------------------------模拟数据---------------------------------
        return Flowable.timer(2, TimeUnit.SECONDS)
                .map {
                    val dataModel = FormListDataModel<${BeanClass}>()
                    dataModel.message = "请求成功"
                    dataModel.code = 200
                    dataModel.data = arrayListOf<${BeanClass}>()
                                        .apply {
                                            add(${BeanClass}())
                                            add(${BeanClass}())
                                            add(${BeanClass}())
                                            add(${BeanClass}())
                                            add(${BeanClass}())
                                            add(${BeanClass}())
                                        }
                    dataModel
                }
        //-----------------------------模拟数据--------------------------------- 
    }
}
