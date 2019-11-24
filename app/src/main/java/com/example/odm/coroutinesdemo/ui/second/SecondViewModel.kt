package com.example.odm.coroutinesdemo.ui.second

import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.*
import com.example.odm.coroutinesdemo.base.BaseViewModel
import com.example.odm.coroutinesdemo.bean.Banner
import com.example.odm.coroutinesdemo.model.Result
import com.example.odm.coroutinesdemo.net.WanRetrofitClient
import com.example.odm.coroutinesdemo.net.WanService
import com.example.odm.coroutinesdemo.ui.second.SecondViewModel.Companion.tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.URI

class SecondViewModel (private val repository: SecondRepository) : BaseViewModel() {

    //viewModel内部更新的MutableLiveData
    private val _bannerList  =  MutableLiveData<List<Banner>>()
    //供外部调用的LiveData
    val bannerList : LiveData<List<Banner>> = _bannerList
    private val _downLoadState = MutableLiveData<String>()
    val downLoadState = _downLoadState
    private val _picDownLoadImg = MutableLiveData<Uri>()
    val picDownLoadImg = _picDownLoadImg


    companion object{
        const val tag = "SecondViewModel"
    }

    init {
        //初始化ViewModel时自动拉取Banner数据
        getBannerData()
    }


    fun getBannerData() {
//        viewModelScope.launch {
//            val result = repository.suspendGetBannerData()
//            if(result  is Result.Success) {
//                _bannerList.value = result.data
//            }
//        }
        launch{
            val result = repository.suspendGetBannerData()
            if(result  is Result.Success) {
                _bannerList.value = result.data
            }
        }
    }


    /**
     * 需要添加Gradle依赖才可启用 LiveDataScope
     * LiveData不可变比较适合每次加载ViewModel时只加载一次的数据,
     */
//    val bannerList : LiveData<List<Banner>>  = liveData {
//        kotlin.runCatching {
//            val result = repository.suspendGetBannerData()
//            if(result  is Result.Success) {
//                emit(result.data)
//            }
//        }
//    }

    /**
     * 下载数据
     * 获取 ResponseBody
     */
    fun downLoadData(url : String) {
        launch {
            val  result = repository.suspendDownLoadData(url)
            if(result.isSuccessful) {
                Log.e(tag, "Response成功返回")
                //切到 IO 线程下 进行文件写入
                launch {
                    saveDownLoadFile(result.body() ,url)
                }
            }
/*---------------上面采用纯协程切换方法，下面采用回调+协程切换----------------------------*/

//                val call = WanRetrofitClient.service.downLoad(url)
//                Log.e(tag , "发出请求")
//                call.enqueue(object : retrofit2.Callback<ResponseBody> {
//                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                        t.printStackTrace()
//                    }
//
//                    override fun onResponse(
//                        call: Call<ResponseBody>,
//                        response: retrofit2.Response<ResponseBody>
//                    ) {
//                        if(response.isSuccessful) {
//                            Log.e(tag, "Response成功返回")
//                            viewModelScope.launch {
//                                saveDownLoadFile(response.body() ,url)
//                            }
//                            Log.e(tag, "body get!！！")
//                        } else {
//                            Log.e(tag ,"response 返回出错")
//                        }
//                    }
//                })
            }

    }

    /**
     * 创建对应文件
     * ResponseBody-->转换成文件流传入文件
     */
    private suspend fun saveDownLoadFile(body : ResponseBody ? , url : String ) = withContext(Dispatchers.IO) {
        if (body != null ) {
            Log.e(tag ,"创建对应文件")
            val path = Environment.getExternalStoragePublicDirectory("").absolutePath
            val file = File("${path}/Download/${url.substringAfterLast("/")}")
            file.createNewFile()
            var inStream: InputStream? = null
            var outStream: OutputStream? = null
            try {
                Log.e(tag, "开始文件读写！！")
                //以下读写文件的操作和java类似
                inStream = body.byteStream()
                outStream = file.outputStream()
                //文件总长度
                val contentLength = body.contentLength()
                //当前已下载长度
                var currentLength = 0L
                //缓冲区
                val buff = ByteArray(1024)
                var len = inStream.read(buff)
                var percent = 0
                while (len != -1) {
                    outStream.write(buff, 0, len)
                    currentLength += len
                    /*不要频繁的调用切换线程,否则某些手机可能因为频繁切换线程导致卡顿,
                这里加一个限制条件,只有下载百分比更新了才切换线程去更新UI*/
                    if ((currentLength * 100 / contentLength).toInt() > percent) {
                        percent = (currentLength / contentLength * 100).toInt()
                        //切换到主线程更新UI
                        launch {
                             updateDownLoadState(currentLength , contentLength ,file)
                            //更新完成UI之后立刻切回IO线程
                        }
                    }
                    len = inStream.read(buff)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                inStream?.close()
                outStream?.close()

            }
        } else {
            Log.e(tag, "response出错了或者 body为空")
        }
    }

    private suspend fun updateDownLoadState(currentLength : Long ,contentLength: Long ,file : File ) = withContext(Dispatchers.Main) {
        _downLoadState.value = "正在将文件流转换为文件:$currentLength / $contentLength"
        Log.e(tag , "正在将文件流转换为文件:$currentLength / $contentLength")
        if(currentLength == contentLength) {
            _downLoadState.value = "下载完成"
             updateDownLoadImage(file)
        }
    }

    private fun updateDownLoadImage(downLoadImg : File ) {
        _picDownLoadImg.value  = Uri.fromFile(downLoadImg)
    }

}
