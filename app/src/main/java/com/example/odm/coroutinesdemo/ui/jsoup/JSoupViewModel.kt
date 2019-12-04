package com.example.odm.coroutinesdemo.ui.jsoup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.odm.coroutinesdemo.base.BaseViewModel

/**
 * @description: JSoup模块的ViewModel层
 * @author: ODM
 * @date: 2019/12/4
 */
class JSoupViewModel(private val repository: JSoupRepository) : BaseViewModel() {

    companion object{
        const val tag = "JSoupViewModel"
    }

    private val _sentenceSoup = MutableLiveData<String>()
    val sentenceSoup : LiveData<String> = _sentenceSoup

    init {
        getSoupSentence()
    }

    fun getSoupSentence(){

        launch {
            val result = repository.getSoupSentenceData() ?: "暂时无法获取:( ，请稍后重试"
            _sentenceSoup.value = result
            Log.e(tag ,"获取到的毒鸡汤：  "+ result)
        }

    }


}