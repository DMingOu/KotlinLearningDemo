package com.example.odm.coroutinesdemo

import com.example.odm.coroutinesdemo.Application.MyApp

/**
 * @description: 常量类
 * @author: ODM
 * @date: 2019/11/24
 */

object Constants {
    val DOWNLOAD_PATH = MyApp.CONTEXT.getExternalFilesDir(null)?.absolutePath+ "/Download"
}

