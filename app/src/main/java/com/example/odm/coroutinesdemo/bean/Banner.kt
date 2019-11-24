package com.example.odm.coroutinesdemo.bean

/**
 * @description: Banner实体类
 * @author: ODM
 * @date: 2019/11/17
 */

data class Banner(val desc: String,
                  val id: Int,
                  val imagePath: String,
                  val isVisible: Int,
                  val order: Int,
                  val title: String,
                  val type: Int,
                  val url: String)