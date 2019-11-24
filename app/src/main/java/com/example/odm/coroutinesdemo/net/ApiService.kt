package com.example.odm.coroutinesdemo.net

import com.example.odm.coroutinesdemo.bean.Banner
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @description: Retrofit 的接口
 * @author: ODM
 * @date: 2019/11/17
 */

interface ApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

//    @GET("/article/list/{page}/json")
//    suspend fun getHomeArticles(@Path("page") page: Int): ApiResponse<ArticleList>

    @GET("/banner/json")
    suspend fun getBanner(): ApiResponse<List<Banner>>


    @Streaming
    @GET
    //接口方法 suspend 会导致下载长时间阻塞，无法接收回调
      fun downLoad(@Url url: String) : Call<ResponseBody>


}