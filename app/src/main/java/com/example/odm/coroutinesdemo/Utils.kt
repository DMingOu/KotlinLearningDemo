package com.example.odm.coroutinesdemo

import android.util.Log
import android.view.View
import android.widget.Checkable

/**
 * @description: 工具类，扩展
 * @author: ODM
 * @date: 2019/11/26
 */

// 扩展点击事件的属性(重复点击时长)，通过setTag方法方便获取
var <T : View> T.lastClickTime: Long
    set(value) = setTag(R.id.tag_lastClickTime, value)
    get() = getTag(R.id.tag_lastClickTime) as? Long ?: 0
// 重复点击事件绑定
inline fun <T : View> T.singleClick(interval: Long = 800, crossinline block: (T) -> Unit) {
        val currentTimeMillis = System.currentTimeMillis()
        Log.e("单次点击事件扩展属性" , "current  "+currentTimeMillis + "      lastClickTime " + lastClickTime)
        //两次点击事件时间间隔大于时间差 || 控件属于选择框一类可多次点击
        if (currentTimeMillis - lastClickTime > interval || this is Checkable) {
            lastClickTime = currentTimeMillis
            Log.e("单次点击事件扩展属性","执行View的点击事件")
            block(this)
    }
}