package com.example.odm.coroutinesdemo.net

import com.example.odm.coroutinesdemo.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @description: 基类RetrofitClient
 * @author: ODM
 * @date: 2019/11/17
 */


abstract class BaseRetrofitClient {

    companion object {
        private const val TIME_OUT = 5
    }

    private val client: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
            } else {
                logging.level = HttpLoggingInterceptor.Level.BASIC
            }

            builder.addInterceptor(logging)
                .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)

            handleBuilder(builder)

            return builder.build()
        }

    /**
     * 定制OkHttpClient的处理策略
     */
    protected abstract fun handleBuilder(builder: OkHttpClient.Builder)

    /**
     * 创建Retrofit实例
     */
    fun <S> getService(serviceClass: Class<S>, baseUrl: String): S {
        return Retrofit.Builder()
                       .client(client)
                       .addConverterFactory(GsonConverterFactory.create())
//                     .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                     .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
                       .baseUrl(baseUrl)
                       .build()
                       .create(serviceClass)
    }
}