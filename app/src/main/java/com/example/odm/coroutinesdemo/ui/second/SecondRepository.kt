package com.example.odm.coroutinesdemo.ui.second

import com.example.odm.coroutinesdemo.base.BaseRepository
import com.example.odm.coroutinesdemo.net.WanRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @description: 第二个页面的数据仓库
 * @author: ODM
 * @date: 2019/11/21
 */

class SecondRepository : BaseRepository() {

    suspend fun suspendGetBannerData() = withContext(Dispatchers.IO) {

//        WanRetrofitClient.service.getBanner().data
        safeApiRequest(request = { executeResponse(response = WanRetrofitClient.service.getBanner() , errorBlock = { println("网络出错")} ) } ,
                    errorMessage = "")

    }


     suspend fun suspendDownLoadData(url : String) = withContext(Dispatchers.IO) {

        WanRetrofitClient.service.downLoad(url).execute()
    }





}