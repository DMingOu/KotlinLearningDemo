package com.example.odm.coroutinesdemo.net

import com.example.odm.coroutinesdemo.Application.MyApp
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import java.io.File

/**
 * @description: RetrofitClient实现类，实现缓存
 * @author: ODM
 * @date: 2019/11/17
 */

object WanRetrofitClient : BaseRetrofitClient() {

    //实际调用实例
    val service by lazy { getService(
        WanService::class.java,
        WanService.BASE_URL
    ) }
    //缓存
    val httpCacheDirectory = File(MyApp.CONTEXT.cacheDir, "responses")
    val cacheSize = 10 * 1024 * 1024L // 10 MiB
    val cache = Cache(
        httpCacheDirectory,
        cacheSize
    )

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        builder.cache(cache)
                .addInterceptor { chain ->
                var request = chain.request()
                if (!NetWorkUtils.isNetworkAvailable(
                        MyApp.CONTEXT
                    )
                ) {
                    request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
                }
                val response = chain.proceed(request)
                if (!NetWorkUtils.isNetworkAvailable(
                        MyApp.CONTEXT
                    )
                ) {
                    val maxAge = 60 * 60
                    response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=$maxAge")
                        .build()
                } else {
                    val maxStale = 60 * 60 * 24 * 28 // 缓存4周28天时间
                    response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .build()
                }

                response
            }
    }
}