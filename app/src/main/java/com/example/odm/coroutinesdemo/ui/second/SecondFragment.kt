package com.example.odm.coroutinesdemo.ui.second

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.odm.coroutinesdemo.R
import org.koin.android.ext.android.inject

/**
 * 测试 协程 + Retrofit + ViewModel + Repository
 * APi调用获取Json
 * 下载文件，保存本地文件，获取进度，完成后加载文件 （以图片形式）
 */
class SecondFragment : Fragment() {

     var tvData : TextView ?= null
     var btnRefreshData : Button ?= null
     lateinit var btnDownLoad : Button
     lateinit var ivShow : ImageView
    companion object {
        fun newInstance() = SecondFragment()
        const val TAG = "SecondFragment"
    }
    //依赖注入获取ViewModel实例
    private val viewModel: SecondViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(FragmentViewModel::class.java)
        initView()
        initViewClickEvent()
        initObservableLiveData()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 初始化控件
     */
    fun initView() {
        tvData = activity?.findViewById(R.id.tv_lay_data)
        btnRefreshData = activity?.findViewById(R.id.btn_refresh_banner)
        btnDownLoad = activity?.findViewById(R.id.btn_download)?: Button(activity)
        ivShow  = activity?.findViewById(R.id.iv_show_download)?: ImageView(activity)
    }

    /**
     * 初始化控件点击事件
     */
    fun initViewClickEvent() {
        btnRefreshData?.setOnClickListener {
            //调用ViewModel方法，刷新Banner数据
            viewModel.getBannerData()
        }
        btnDownLoad.setOnClickListener {
            //下载网络资源
            viewModel.downLoadData("https://www.wanandroid.com/blogimgs/90c6cc12-742e-4c9f-b318-b912f163b8d0.png")
        }

    }

    private fun initObservableLiveData() {
        //对ViewModel的数据进行观察，方式1
//        viewModel.bannerList.observe(this , Observer {
//            tvData?.text = it?.get(1)?.url
//        } )
        //对ViewModel的数据进行观察，方式2
        viewModel.apply {
            bannerList.observe(this@SecondFragment , Observer {
                it?.let { setText(textView = tvData , content = it.toString() )                }
            } )
            downLoadState.observe(this@SecondFragment  , Observer {
                it?.let { setText(textView = tvData ,content = it) }
            })
            picDownLoadImg.observe( this@SecondFragment , Observer {
                it?.let {
                    ivShow.setImageURI(it)
                }
            })
        }
    }

    private fun setText(textView : TextView? , content : String = "") {
        textView?.text = content
    }
}
