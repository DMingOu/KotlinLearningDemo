package com.example.odm.coroutinesdemo.base

import com.example.odm.coroutinesdemo.model.Result
import com.example.odm.coroutinesdemo.net.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.io.IOException

/**
 * @description: 数据仓库基类
 * @author: ODM
 * @date: 2019/11/21
 */
open class BaseRepository {


    /**
     * 将网络异常捕捉包装成IOException的网络调用方法
     * request:具体的请求方法 executeResponse
     * return：Result类型
     */
    suspend fun <T : Any> safeApiRequest(request: suspend () -> Result<T>, errorMessage: String): Result<T> {
        return try {
            request()
        } catch (exception: Exception) {
            // 当调用API抛出一个异常,,将它转换为一个IOException
            Result.Error(
                IOException(
                    errorMessage,
                    exception
                )
            )
        }
    }

    /**
     * errorCode == -1 要针对特定的业务
     * 根据状态码，协程执行success代码块和error代码块
     * 必须传入参数为ApiResponse类型
     * return: Result类型
     */
    suspend fun <T : Any> executeResponse(response: ApiResponse<T>,
                                          successBlock: (suspend CoroutineScope.() -> Unit)? = null,
                                          errorBlock: (suspend CoroutineScope.() -> Unit)? = null)
                                         : Result<T> {
        return coroutineScope {
            if (response.errorCode == -1) {
                errorBlock?.let { it() }
                Result.Error(
                    IOException(
                        response.errorMsg
                    )
                )
            } else {
                successBlock?.let { it() }
                Result.Success(response.data)
            }
        }
    }


}