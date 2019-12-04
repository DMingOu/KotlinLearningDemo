package com.example.odm.coroutinesdemo.ui.jsoup

import com.example.odm.coroutinesdemo.base.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.select.Elements


/**
 * @description: JSou模块的数据仓库层
 * @author: ODM
 * @date: 2019/12/4
 */
class JSoupRepository : BaseRepository(){

    suspend fun getSoupSentenceData() : String = withContext(Dispatchers.IO) {
        val doc = Jsoup.connect("http://www.nows.fun/").get()
        val elements: Elements = doc.select("div[class=container main-sentence justify-content-center text-center] > span")
        elements.text()
    }


}