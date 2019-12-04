package com.example.odm.coroutinesdemo.ui.jsoup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.odm.coroutinesdemo.R
import com.example.odm.coroutinesdemo.singleClick
import org.koin.android.ext.android.inject

/**
 * @description: Jsoup模块 View层
 * @author: ODM
 * @date: 2019/12/4
 */
class JSoupFragment : Fragment() {

    companion object{
        fun newInstance() : JSoupFragment = JSoupFragment()
        const val tag = "JSoupFragment"
    }

    var tvSoup : TextView?= null
    var btnRefreshData : Button?= null

    //依赖注入获取ViewModel实例
    private val viewModel: JSoupViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_jsoup, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(FragmentViewModel::class.java)
        initView()
        initViewClickEvent()
        initObservableLiveData()
    }

    private fun initView() {
        tvSoup = activity?.findViewById(R.id.tv_soup)
        btnRefreshData = activity?.findViewById(R.id.btn_refresh_soup)
    }

    private fun initViewClickEvent(){
        btnRefreshData?.setOnClickListener {
                //重新获取数据
                viewModel.getSoupSentence()
        }
    }

    private fun initObservableLiveData() {
            viewModel.apply {
                sentenceSoup.observe(this@JSoupFragment , Observer {
                    tvSoup?.text =  it
                })
            }
    }

}