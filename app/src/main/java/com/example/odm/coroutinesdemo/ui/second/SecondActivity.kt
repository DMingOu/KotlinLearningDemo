package com.example.odm.coroutinesdemo.ui.second

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.odm.coroutinesdemo.R
import com.example.odm.coroutinesdemo.ui.second.SecondFragment

/**
 *
 */
class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        if (savedInstanceState == null) {
            //切出显示Fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SecondFragment.newInstance())
                .commitNow()
        }
    }

}
