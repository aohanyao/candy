/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.td.framework.mvp.presenter

import com.td.framework.biz.ApiSubscriber
import com.td.framework.biz.NetError
import com.td.framework.mvp.comm.RequestType
import com.td.framework.mvp.view.BaseView
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.components.RxActivity
import com.trello.rxlifecycle3.components.RxFragment
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragmentActivity
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * 基类P
 */
abstract class BasePresenter<V>(val v: V) {
    protected var subscribe: Disposable? = null
    /**
     * 取消订阅
     *防止内存泄露
     * 用户取消网络请求
     */
    open fun unSubscribe() {
        try {
            subscribe?.dispose()
        } catch (ignored: Exception) {
        }
    }

    /**
     * 开始请求
     */
    @Deprecated("这个请求将不再使用，请使用：request(request: Flowable<T>?, requestType: Int = NetError.RequestType.UNDFINE,\n" +
            "result: (T?) -> Unit)", ReplaceWith("request(request, NetError.RequestType.UNDFINE, result)", "com.td.framework.biz.NetError"))
    protected fun <T> request(request: Flowable<T>?, result: (T?) -> Unit) {
        request(request, RequestType.UNDFINE, result)
    }

    /**
     * 开始请求
     */
    protected fun <T> request(request: Flowable<T>?, requestType: Int = RequestType.UNDFINE,
                              result: (T?) -> Unit) {
        request(request, requestType, result, {
            // null block
        })
    }

    /**
     * 开始请求，携带返回值和错误值
     */
    protected fun <T> request(request: Flowable<T>?, requestType: Int = RequestType.UNDFINE,
                              result: (T?) -> Unit, errorBack: () -> Unit) {
        subscribe = request?.compose(this.getCompose())
                ?.subscribeWith(object : ApiSubscriber<T>(v as BaseView, requestType) {
                    override fun onNext(t: T?) {
                        result.invoke(t)
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        errorBack.invoke()
                    }

                    override fun onFail(error: NetError?) {
                        super.onFail(error)
                        errorBack.invoke()
                    }
                })
    }


    /**
     * 已订阅，暂留，用来做后续的优化
     */
    open fun subscribe() {

    }

    /**
     *  *  1. 线程切换
     *  *  2. Rx生命周期绑定
     * @return
     */
    protected fun <T> getCompose(): FlowableTransformer<T, T> {
        return FlowableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose<T>(when (v) {
                        is RxAppCompatActivity -> v.bindUntilEvent<T>(ActivityEvent.DESTROY)
                        is RxFragmentActivity -> v.bindUntilEvent<T>(ActivityEvent.DESTROY)
                        is RxActivity -> v.bindUntilEvent<T>(ActivityEvent.DESTROY)
                        is RxFragment -> v.bindUntilEvent<T>(FragmentEvent.DESTROY)
                        is com.trello.rxlifecycle3.components.support.RxFragment -> v.bindUntilEvent<T>(FragmentEvent.DESTROY)
                        else -> (v as RxAppCompatActivity).bindUntilEvent<T>(ActivityEvent.DESTROY)
                    })
        }

    }

}
