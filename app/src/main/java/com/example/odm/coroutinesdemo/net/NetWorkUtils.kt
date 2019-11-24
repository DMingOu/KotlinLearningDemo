package com.example.odm.coroutinesdemo.net

import android.content.Context
import android.net.ConnectivityManager

/**
 * @description: 网络判断工具类
 * @author: ODM
 * @date: 2019/11/17
 */

class NetWorkUtils {

    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val manager = context.applicationContext.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = manager.activeNetworkInfo
            return !(null == info || !info.isAvailable)
        }
    }
}