package com.example.odm.coroutinesdemo.ui.media

import android.media.AudioManager
import android.media.Image
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.odm.coroutinesdemo.Application.MyApp
import com.example.odm.coroutinesdemo.R
import java.io.IOException
import java.lang.IllegalStateException
import java.net.URI

/**
 * @description: MediaPlayer测试
 * @author: ODM
 * @date: 2019/11/23
 */

class MediaFragment : Fragment() ,MediaPlayer.OnPreparedListener , SurfaceHolder.Callback{

    var mediaPlayer: MediaPlayer ?= null
    var btnPlayMusic : Button ?= null
    var btnNext : ImageButton ?= null
    var sfvShow : SurfaceView ?= null
    var surfaceHolder: SurfaceHolder ?= null
    //网络歌曲URL列表
    var songs : List<String> = listOf("http://dl.stream.qqmusic.qq.com/C400001Qu4I30eVFYb.m4a?vkey=5E8F54F63CC5B796EE40289DD777946084A565AF2996948883D10091DEB89FC47F587B90A8917F456DD080720D8EE3569835F85CEF9E4236&guid=7332953645&uin=1297716249&fromtag=66",
        "http://m10.music.126.net/20191123205234/8db8058e854e1d8f663c7a5a5e8a9460/ymusic/e33e/ce89/f6b0/03021c4140edc953808280ac78bd35be.mp3",
        "http://music.163.com/song/media/outer/url?id=25906124.mp3")
    val path = Environment.getExternalStoragePublicDirectory("").absolutePath
    companion object{
        fun newInstance() = MediaFragment()
        const val tag = "MediaFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_media ,container ,false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    override fun onStart() {
        super.onStart()
        mediaPlayer = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC) //设置音频为 音乐
        }
    }

    override fun onStop() {
        super.onStop()
        //暂停播放
        if (mediaPlayer!!.isLooping){
            mediaPlayer?.stop()
        }
        //释放mediaPlayer
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun initView() {
        btnPlayMusic = activity?.findViewById(R.id.btn_play_sound)
        btnPlayMusic?.setOnClickListener {
            Log.e(tag ,"设置点击事件")
            mediaPlayer?.let {
                Log.e(tag , "点击了播放键")
                //加载本地文件路径的音视频
                mediaPlayerPrepare(url = path + "/jay_dont_cry.mp3")
                //加载网络URL，会出异常 E/MediaPlayerNative: error (1, -2147483648)
//                  mediaPlayerPrepare(url = songs[0])
            }

        }
        btnNext = activity?.findViewById(R.id.iBtn_next)
        btnNext?.setOnClickListener {
            Log.e(tag , "点击了下一首")
        }
        sfvShow  = activity?.findViewById(R.id.sfv_show)
        surfaceHolder  = sfvShow?.holder
        surfaceHolder?.addCallback(this)

    }

    private fun mediaPlayerPrepare(url : String ?= null , uri : Uri?= null ) {

        mediaPlayer?.apply {
            //在非播放状态或者未设置音频路径才设置数据源
            if( ! isPlaying) {
                try {
                    url?.let {
                        Log.e(tag ,"准备音频的路径URL：   "+it)
                        setDataSource(it)
                    }
                    uri?.let {
                        setDataSource(MyApp.CONTEXT ,it)
                    }
                    setOnPreparedListener(this@MediaFragment)
                }catch (e : IOException) {
                    e.printStackTrace()
                    throw(e)
                }catch (e1 : IllegalStateException) {
                    e1.printStackTrace()
                    throw (e1)
                }
                prepareAsync()
            }
        }

    }

    override fun onPrepared(mp: MediaPlayer?) {
        Log.e(tag , "启动MediaPlayer")
        mp?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.e(tag , "surfaceView改变大小")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        Log.e(tag , "surfaceView销毁")
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mediaPlayer?.setDisplay(surfaceHolder)
    }
}