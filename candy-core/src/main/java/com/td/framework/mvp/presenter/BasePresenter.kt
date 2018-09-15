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
import com.td.framework.mvp.view.BaseView
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.RxActivity
import com.trello.rxlifecycle2.components.RxFragment
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragmentActivity
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

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
    protected fun <T> request(request: Flowable<T>?, result: (T?) -> Unit) {
        subscribe = request?.compose(this.getCompose())
                ?.subscribeWith(object : ApiSubscriber<T>(v as BaseView) {
                    override fun onNext(t: T?) {
                            result.invoke(t)
                    }

                })
    }


    fun mapToParams(param:Any): Map<String, String> {
        //创建参数集合
        val mParamsMap = WeakHashMap<String, String>()
        //获取反射
//        param.javaClass.kotlin.memberProperties

        return mParamsMap
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
                        is com.trello.rxlifecycle2.components.support.RxFragment -> v.bindUntilEvent<T>(FragmentEvent.DESTROY)
                        else -> (v as RxAppCompatActivity).bindUntilEvent<T>(ActivityEvent.DESTROY)
                    })
        }

    }

}
