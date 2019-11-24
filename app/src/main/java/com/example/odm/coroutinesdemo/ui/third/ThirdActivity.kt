package com.example.odm.coroutinesdemo.ui.third

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.drm.DrmStore
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Property
import android.view.ActionMode
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.example.odm.coroutinesdemo.R

/**
 * 按钮搭配ObjectAnimator动画测试Sample
 * 用动画的方式实现了按钮按住会缩小，松手时恢复
 */

class ThirdActivity : AppCompatActivity() {

    companion object{
        const val TAG = "ThirdActivity"
    }

    lateinit var iBtnCall: ImageButton
    lateinit var btnPowerOff : Button
    var animatorSet : AnimatorSet ?= null
    //imageButton 调整到屏幕的距离
    val objectAnimator_iBtnCall : ObjectAnimator by lazy {
        ObjectAnimator.ofInt(iBtnCall , "left",0 ,350)
    }
    var width1 : Float = 0.8f
    var width2 = 1.2f
    val width3 = 1.0f
    val objectAnimator_btnPowerOff_Width : ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(btnPowerOff ,"scaleX" , 3.0f)
    }

    val objectAnimator_btnPowerOff_Height : ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(btnPowerOff, "scaleY" ,3.0f)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        initView()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        iBtnCall = findViewById(R.id.iBtn_call)

        btnPowerOff = findViewById(R.id.btn_power_off)
        iBtnCall.setOnClickListener {
            objectAnimator_iBtnCall.setAutoCancel(true)
            objectAnimator_iBtnCall.duration = 1500
            objectAnimator_iBtnCall.start()
        }
        btnPowerOff.setOnClickListener {
        // 启动缩放x , y 动画方式一
////            objectAnimator_btnPowerOff_Width.setAutoCancel(true)
////            objectAnimator_btnPowerOff_Width.duration =  1500
////            objectAnimator_btnPowerOff_Width.start()
////            objectAnimator_btnPowerOff_Height.setAutoCancel(true)
////            objectAnimator_btnPowerOff_Height.duration = 1500
////            objectAnimator_btnPowerOff_Height.start()
//      // 启动缩放x , y 动画方式二
//            animatorSet?.start()
            Log.e(TAG , "测试onTouch事件和onClick事件是否冲突")
        }
//        animatorSet = AnimatorSet()
//        animatorSet?.playTogether(listOf(objectAnimator_btnPowerOff_Width,objectAnimator_btnPowerOff_Height))
        objectAnimator_btnPowerOff_Width.duration =  500
        objectAnimator_btnPowerOff_Height.duration = 500
        //用动画的方式实现按钮按住会缩小，松手时恢复
        btnPowerOff.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.e(TAG , "落下事件触发")
                    objectAnimator_btnPowerOff_Width.setFloatValues(1.0f,0.8f)
                    objectAnimator_btnPowerOff_Height.setFloatValues(1.0f,0.8f)
                    objectAnimator_btnPowerOff_Width.start()
                    objectAnimator_btnPowerOff_Height.start()
                    true//false则可以在Action结束时触发onClick方法，true则代表了拦截
                }

                MotionEvent.ACTION_UP -> {
                    Log.e(TAG , "抬起事件触发")
                    objectAnimator_btnPowerOff_Width.setFloatValues(0.8f,1.0f)
                    objectAnimator_btnPowerOff_Height.setFloatValues(0.8f,1.0f)
//                    objectAnimator_btnPowerOff_Width.reverse()
                    objectAnimator_btnPowerOff_Width.start()
//                    objectAnimator_btnPowerOff_Height.reverse()
                    objectAnimator_btnPowerOff_Height.start()

                    false
                }
                MotionEvent.ACTION_MOVE -> {
                    //因为长按按住按钮的过程会触发这个MOVE事件，所以要对MOVE进行处理
                    false
                }
                else -> {
                    Log.e(TAG , "其他手指事件触发  " )
                    objectAnimator_btnPowerOff_Height.cancel()
                    objectAnimator_btnPowerOff_Width.cancel()
                    false
                }
            }
        }

    }







}
