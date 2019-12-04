package com.example.odm.coroutinesdemo.di

import com.example.odm.coroutinesdemo.ui.jsoup.JSoupRepository
import com.example.odm.coroutinesdemo.ui.jsoup.JSoupViewModel
import com.example.odm.coroutinesdemo.ui.second.SecondViewModel
import com.example.odm.coroutinesdemo.ui.second.SecondRepository
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @description: 依赖注入声明类，可以有多个Module对象
 * @author: ODM
 * @date: 2019/11/21
 */



    //get()获取SecondRepository
    val viewModelModule = module {
        viewModel { SecondViewModel(get()) }
        viewModel { JSoupViewModel(get()) }
    }

    val repositoryModule = module {
        single { SecondRepository() }
        single { JSoupRepository() }
    }

    val AppModule = listOf(viewModelModule, repositoryModule)
