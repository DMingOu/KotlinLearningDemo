package com.example.odm.coroutinesdemo.Application

import android.app.Application
import android.content.Context
import com.example.odm.coroutinesdemo.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import kotlin.properties.Delegates

/**
 * @description: Application类
 * @author: ODM
 * @date: 2019/11/17
 */

 class MyApp : Application() {

    companion object{
        var CONTEXT : Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
        //Koin依赖注入
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            //androidFileProperties()
            modules(AppModule)
        }
    }
}