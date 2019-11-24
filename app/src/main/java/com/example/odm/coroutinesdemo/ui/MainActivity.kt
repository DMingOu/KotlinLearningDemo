package com.example.odm.coroutinesdemo.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.odm.coroutinesdemo.R
import com.example.odm.coroutinesdemo.bean.Banner
import com.example.odm.coroutinesdemo.net.WanRetrofitClient
import com.example.odm.coroutinesdemo.ui.media.MediaFragment
import com.example.odm.coroutinesdemo.ui.second.SecondActivity
import com.example.odm.coroutinesdemo.ui.second.SecondFragment
import com.example.odm.coroutinesdemo.ui.third.ThirdActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.coroutines.*

/**
 * 继承MainScope后可以直接书写launch代码块
 */
class MainActivity : AppCompatActivity() , CoroutineScope by MainScope(){

    lateinit var tvData : TextView
    lateinit var btnGetData : Button
    lateinit var job : Job

    companion object{
        const val tag = "MainActivity"
    }
    var num   = 0
    lateinit var bannerDataList : List<Banner>

    /**
     * demo: 变量的Getter/Setter
     */
    var x = 1000
    get()   =  field +5
    set(value) {field = value+105}





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        Log.e(tag,"x 的值  " + x)
        x = 20000
        Log.e(tag,"x 的值  " + x)
        initPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        //协程的取消
        cancel()
    }

    fun init(){
        tvData = findViewById(R.id.tv_data)
        btnGetData = findViewById(R.id.btn_get_data)
//        coroutineContext = EmptyCoroutineContext
//        coroutineScope = MainScope()
        job = printCurrentThread()
    }



    /**
     * 点击事件
     */
    fun onViewClicked(view : View) {
        when(view.id) {
            //获取Banner数据
            R.id.btn_get_data ->  getBannerData()
            //跳转第二个页面
            R.id.btn_jump_activity2 -> {startActivity(Intent().setClass(this , SecondActivity::class.java))}
            //跳转到第三个页面
            R.id.btn_jump_third -> {  startActivity(Intent().setClass(this , ThirdActivity::class.java)) }
            //跳转到音频播放页面
            R.id.btn_jump_media -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_main_contain, MediaFragment.newInstance())
                    .commitNow()
            }
        }
    }

    fun printCurrentThread() : Job {
        return  launch(Dispatchers.Main) {
            num++
            Log.e(tag,"当前num ：" + num)
            Log.e(tag,"当前线程为" + Thread.currentThread().name)
            launch(Dispatchers.IO) {
                num++
                Log.e(tag,"当前num ：" + num)
                Log.e(tag,"当前线程为" + Thread.currentThread().name)
            }
            Log.e(tag,"当前num ：" + num)
            withContext(Dispatchers.IO) {
                delay(1500)
                num++
            }
            //
            Log.e(tag,"当前线程为" + Thread.currentThread().name)
            Log.e(tag,"当前num ：" + num)
            withContext(Dispatchers.Main) {
                num++
            }
            Log.e(tag,"当前线程为" + Thread.currentThread().name)
            Log.e(tag,"当前num ：" + num)
        }
    }


    fun getBannerData() {
        launch {
            bannerDataList =  suspendGetBannerData()
            val string = bannerDataList[2].title+" "+bannerDataList[2].imagePath
            tvData.text  =  string
        }
        //以下这行会报错，因为协程被挂起，list还没获取到数据，会空指针
        //Log.e(tag , bannerDataList[0].title)
    }

    suspend fun suspendGetBannerData() = withContext(Dispatchers.IO) {

         WanRetrofitClient.service.getBanner().data
    }

    @SuppressLint("CheckResult")
    private fun initPermissions(){
        val rxPermissions : RxPermissions = RxPermissions(this)
        rxPermissions.request(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe{
                if (it) {
                    Log.e(tag, "accept: 动态申请 权限回调 true" );
                } else {
                    Log.e(tag, "accept: 动态申请 权限回调 false" );
                    Toast.makeText(this,"未授权应用相关权限，将无法使用拍照识别功能！",Toast.LENGTH_LONG).show();
                }
            }
    }


 }
