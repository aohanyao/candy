package ${packageName}.view.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.td.framework.mvp.base.MvpFormListDataBaseFragment
import kotlinx.android.synthetic.main.${simpleLayoutName}.*
import ${packageName}.presenter.${presenterClass}
import ${packageName}.view.adapter.${AdapterClass}
import ${BeanPackageName}.${BeanClass}
import ${ParamPackageName}.${ParamClass}

/**
 * Created on ${.now}
 * @author: gongziyi
 * @version:1.0
 * Description:${fragmentTitle}
 */
class ${fragmentClass} : MvpFormListDataBaseFragment<${presenterClass}, ${BeanClass}>() {


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
  }


    override fun newP(): ${presenterClass}? {
        return ${presenterClass}(this)
    }

    override fun getSwipeRefreshLayout(): View? {
        return ${refreshLayout}
    }

    override fun getAdapter(): BaseQuickAdapter<${BeanClass}, *>? {
        return ${AdapterClass}(mDatas)
    }

    override fun getLayoutId(): Int {
      <#if modlueName?length gt 1>
          return R.layout.${simpleLayoutName}_${modlueName}
      <#else>
          return R.layout.${simpleLayoutName}
      </#if>

    }
    override fun getRecyclerView(): RecyclerView? {
        return ${recyclerView}
    }

    override fun getParam(): ${ParamClass} {
        return ${ParamClass}()
    }
    override fun onRecyclerViewItemChildClick(view: View, position: Int, data: ${BeanClass}) {

    }

    companion object {
      /**
       * 获取${fragmentTitle}的实例
       */
        fun newInstance(): ${fragmentClass} {
            val args = Bundle()
            val fragment = ${fragmentClass}()
            fragment.arguments = args
            return fragment
        }
    }
}
