package com.td.framework.biz;

import android.text.TextUtils;

import com.td.framework.NetConfig;
import com.td.framework.biz.interceptor.XInterceptor;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 基类  API
 */
public class BaseApi {
    /**
     * 存放认证参数的key
     */
    public static String AUTHORIZATION = "authorization_key";
    /**
     * 用来刷新的token
     */
    public static String REFRESH_TOKEN = "user_refresh_token";

    public static NetProvider provider = null;
    private Retrofit retrofit = null;
    private OkHttpClient client = null;
    private static final long connectTimeoutMills = 40 * 1000L;
    private static final long readTimeoutMills = 40 * 1000L;

    private static BaseApi instance;

    private BaseApi() {
        retrofit = null;
        client = null;
    }


    /**
     * 创建 Retrofit
     *
     * @return
     */
    public static synchronized Retrofit createRetrofit() {
        return getInstance().getRetrofit(true);
    }

    public static synchronized BaseApi getInstance() {
        if (instance == null)
            instance = new BaseApi();
        return instance;
    }


    /**
     * 统一RUL
     *
     * @param service 服务
     * @param <S>     泛型
     * @return 泛型
     */
    public static <S> S get(Class<S> service) {
        return getInstance().getRetrofit(true).create(service);
    }

    /***
     * 外部传递url
     *
     * @param service 服务
     * @param baseUrl url
     * @param <S> 泛型
     * @return 泛型
     */
    public static <S> S get(Class<S> service, String baseUrl) {
        return getInstance().getRetrofit(true, baseUrl).create(service);
    }

    /**
     * 注册配置提供者
     *
     * @param provider
     */
    public static void registerConfigProvider(NetProvider provider) {
        BaseApi.provider = provider;
        //赋值debug
        NetConfig.isDebug = provider.configLogEnable();
        //赋值为空
        instance = null;
    }


    /**
     * 获取Retrofit
     *
     * @param useRx
     * @return
     */
    public Retrofit getRetrofit(boolean useRx) {
//        checkProvider();
        if (retrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(provider.configBaseUrl())
                    .client(getHttpClient())
                    .addConverterFactory(provider.configConverterFactory() == null ? GsonConverterFactory.create() : provider.configConverterFactory());
            if (useRx) {
                builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            }
            retrofit = builder.build();
        }
        return retrofit;
    }

    /**
     * 外部，传递url
     *
     * @param useRx
     * @param baseUrl
     * @return
     */
    public Retrofit getRetrofit(boolean useRx, String baseUrl) {
//        checkProvider();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getHttpClient())
                .addConverterFactory(provider.configConverterFactory() == null ? GsonConverterFactory.create() : provider.configConverterFactory());
        if (useRx) {
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        }
        return builder.build();
    }

    /**
     * 获取httpclient
     *
     * @return
     */
    private OkHttpClient getHttpClient() {
//        checkProvider();
        if (client == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.connectTimeout(provider.configConnectTimeoutMills() != 0
                    ? provider.configConnectTimeoutMills()
                    : connectTimeoutMills, TimeUnit.MILLISECONDS);
            builder.readTimeout(provider.configReadTimeoutMills() != 0
                    ? provider.configReadTimeoutMills() : readTimeoutMills, TimeUnit.MILLISECONDS);

            CookieJar cookieJar = provider.configCookie();
            if (cookieJar != null) {
                builder.cookieJar(cookieJar);
            }
            provider.configHttps(builder);

            RequestHandler handler = provider.configHandler();
            if (handler != null) {
                builder.addInterceptor(new XInterceptor(handler));
            }
            Interceptor[] interceptors = provider.configInterceptors();
            if (interceptors != null && interceptors.length > 0) {
                for (Interceptor interceptor : interceptors) {
                    builder.addInterceptor(interceptor);
                }
            }


            if (provider.configLogEnable()) {//打印log？
                HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
                logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(logInterceptor);
            }

            client = builder.build();
        }
        return client;
    }


    /**
     * 检查提供者
     */
    private static void checkProvider() {
        if (provider == null
                || TextUtils.isEmpty(provider.configBaseUrl())) {
            throw new IllegalStateException("must register provider first");
        }
    }

    /**
     * 获得提供者
     *
     * @return NetProvider
     */
    public static NetProvider getProvider() {
        return provider;
    }

    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> getScheduler() {    //compose简化线程
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
