package com.aohanyao.framework.global

import com.td.framework.biz.BaseApi
import com.td.framework.biz.NetError
import com.td.framework.biz.NetProvider
import com.td.framework.biz.RequestHandler
import com.td.framework.global.app.App
import com.td.framework.utils.L
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.converter.gson.CustGsonConverterFactory

/**
 * Created by 江俊超 on 2017/9/26.
 * Version:1.0
 * Description:
 * ChangeLog:
 */
class MvpApplication : App() {
    override fun onCreate() {
        super.onCreate()
        BaseApi.registerConfigProvider(object : NetProvider {
            override fun configConverterFactory(): Converter.Factory {
                return CustGsonConverterFactory.create()
            }

            override fun configInterceptors(): Array<Interceptor> {
                return arrayOf()
            }

            override fun configHandler(): RequestHandler? {
                return null
            }

            override fun configLogEnable(): Boolean {
                return L.isDebug
            }

            override fun configBaseUrl(): String {
                return "http://120.24.164.29/"

            }

            override fun configCookie(): CookieJar? {
                return null
            }

            override fun configHttps(builder: OkHttpClient.Builder?) {
            }

            override fun configConnectTimeoutMills(): Long {
                return 45 * 1000
            }

            override fun configReadTimeoutMills(): Long {
                return 45 * 1000

            }

            override fun handleError(error: NetError?): Boolean {
                return false
            }
        })
    }
}