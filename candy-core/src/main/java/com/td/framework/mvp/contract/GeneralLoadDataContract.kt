package com.td.framework.mvp.contract

import com.td.framework.model.bean.CodeMsgModel
import com.td.framework.model.bean.FormListDataModel
import com.td.framework.model.bean.ListDataModel
import com.td.framework.mvp.comm.RequestType
import com.td.framework.mvp.model.BaseParamsInfo
import com.td.framework.mvp.presenter.BasePresenter
import com.td.framework.mvp.view.BaseView
import io.reactivex.Flowable

/**
 * Created by jc
 *
 * 版本:1.0.0
 * **说明**通用加载数据合同<br></br>
 *
 *  *  jc：  2018年1月3日09:19:31
 *  *  有一些问题，没有考虑好分页的情况，加了一个接口的方式，希望能够解决这个问题。 待定。
 *  *
 **** */
interface GeneralLoadDataContract {
    /**
     * 通用加载数据的View层
     *
     * @param T 泛型 返回的实体数据
     */
    interface GeneralLoadDataView<out T> : BaseView {

        /**
         * 刷新数据

         * @param datas
         */
        fun refreshSuccess(datas: List<@UnsafeVariance T>)

        /**
         * 加载更多数据

         * @param datas
         */
        fun loadMoreSuccess(datas: List<@UnsafeVariance T>)

        /**
         * 提交数据成功
         */
        fun commitDataSuccess()

        /**
         * 提交数据失败
         */
        fun commitDataFail()

        /**
         * 已经没有数据了
         */
        fun noMore()
    }

    /**
     * 通用的加载数据P层
     * @param V View 层，必须继承GeneralLoadDataView
     * @param T 数据对象
     * */
    abstract class GeneralLoadDataPresenter<V : GeneralLoadDataView<*>, T,
            out RP : BaseParamsInfo>(view: V) : BasePresenter<V>(view) {
        /**
         * 每页大小
         */
        /**
         * 获取每页大小

         * @return
         */
        protected val pageSize = 20
        /**
         * 页码
         */
        protected var mPagerIndex = 1

        /**
         * 刷新数据
         *
         * 自动判断PageSize和PageIndex
         * 可覆写getPageSize() 方法 设置页大小
         */
        open fun refreshData(params: @UnsafeVariance RP) {
            //页码
            mPagerIndex = 1
            //取消前一次请求
            unSubscribe()
            //开始请求
            request(getRequestDataObservable(params), RequestType.REFRESH_LIST) {
                //强转类型
                val list = it?.list ?: arrayListOf()
                //返回数据
                v.refreshSuccess(list)
                //计算是否有更多
                if (list.isEmpty() || mPagerIndex >= it?.maxPage ?: mPagerIndex) {
                    v.noMore()
                }
            }
        }

        /**
         *
         * 自动判断PageSize和PageIndex
         * 可覆写getPageSize() 方法 设置页大小
         */
        open fun loadMoreData(params: @UnsafeVariance RP) {
            //页码
            mPagerIndex++
            //取消前一次请求
            unSubscribe()
            //开始请求
            request(getRequestDataObservable(params), RequestType.LOAD_MORE_LIST) {
                //强转类型
                val list = it?.list ?: arrayListOf()
                //返回数据
                v.loadMoreSuccess(list)
                //计算是否有更多
                if (list.isEmpty() || mPagerIndex >= it?.maxPage ?: mPagerIndex) {
                    v.noMore()
                }
            }
        }

        /**
         * 提交数据
         */
        open fun <TRP : BaseParamsInfo> commitData(param: TRP) {
            //取消请求
            unSubscribe()
            //开始请求
            getCommitDataObservable(param)?.apply {
                request(this, RequestType.POST) {
                    v.complete("")
                    if (it?.code == 200) {
                        v.commitDataSuccess()
                    } else {
                        v.commitDataFail()
                    }
                }
            }
        }

        /**
         * 转换数据结构
         * @param it 必须是ListDataModel<T>的实现类
         */
        protected fun mapListData(it: Any?): ListDataModel<T> {
            if (it !is ListDataModel<*>) {
                throw RuntimeException("the it must is ListDataModel")
            }

            return object : ListDataModel<T> {
                override val maxPage: Int
                    get() = if (it?.list == null) {
                        0
                    } else {
                        it.maxPage
                    }
                override val list: List<T>
                    get() = if (null == it || null == it.list) {
                        arrayListOf()
                    } else {

                        it.list as List<T>
                    }
            }
        }

        /**
         * 请返回获取数据的Observable
         * @return
         */
        protected abstract fun getRequestDataObservable(params: @UnsafeVariance RP): Flowable<ListDataModel<T>>?

        /**
         * 提交数据的类型
         */
        protected open fun <TRP : BaseParamsInfo> getCommitDataObservable(params: TRP): Flowable<CodeMsgModel>? {
            return null
        }
    }

    /**
     * 通用的加载数据P层
     * @param V View 层，必须继承GeneralLoadDataView
     * @param T 数据对象
     * */
    abstract class GeneralFormDataPresenter<V : GeneralLoadDataView<*>, T,
            out RP : BaseParamsInfo>(view: V) : GeneralLoadDataPresenter<V, T, RP>(view) {
        /**
         * 刷新数据
         *
         */
        override fun refreshData(params: @UnsafeVariance RP) {
            //取消前一次请求
            unSubscribe()
            //开始请求
            request(getRequestFormDataObservable(params), RequestType.REFRESH_LIST) {
                if (it?.code == 200) {
                    //强转类型
                    val list = it?.data ?: arrayListOf()
                    //返回数据
                    v.refreshSuccess(list)
                    v.noMore()
                } else {
                    v.noMore()
                }
            }
        }

        /**
         *
         * 自动判断PageSize和PageIndex
         * 可覆写getPageSize() 方法 设置页大小
         */
        override fun loadMoreData(params: @UnsafeVariance RP) {
            refreshData(params)
        }


        /**
         * 请返回获取数据的Observable
         * @return
         */
        protected abstract fun getRequestFormDataObservable(params: @UnsafeVariance RP): Flowable<FormListDataModel<T>>?

        override fun getRequestDataObservable(params: @UnsafeVariance RP): Flowable<ListDataModel<T>>? {
            return null
        }
    }
}
