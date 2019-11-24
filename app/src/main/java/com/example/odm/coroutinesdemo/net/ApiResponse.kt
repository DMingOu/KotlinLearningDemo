package com.example.odm.coroutinesdemo.net

/**
 * @description: 接口返回数据包装类
 * @author: ODM
 * @date: 2019/11/17
 */


data class ApiResponse<out T>(val errorCode : Int, val errorMsg : String, val data : T)