package com.example.odm.coroutinesdemo.model

/**
 * @description: 实体类结果密封类
 * @author: ODM
 * @date: 2019/11/17
 */

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}