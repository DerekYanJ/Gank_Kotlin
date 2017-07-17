package com.yqy.gank.ui.activity

import android.view.View
import com.yqy.gank.R
import com.yqy.gank.bean.GirlBean
import com.yqy.gank.frame.BaseActivity
import com.yqy.gank.http.HttpRequest
import com.yqy.gank.http.ProgressSubscriber
import com.yqy.gank.listener.OnClickBackListener
import com.yqy.gank.utils.L

class MainActivity : BaseActivity() {
    var mList: List<GirlBean> = ArrayList()

    override fun preView(): Int = R.layout.activity_main

    override fun initView() {
    }

    override fun addListener() {
    }

    override fun initData() {
        req()
    }

    override fun getOnBackClickListener(): OnClickBackListener = null!!

    override fun onClick(v: View?) {
    }

    fun req() {
        val params = HashMap<String, String>()
        HttpRequest.getGirls(
                ProgressSubscriber<List<GirlBean>>(this, this, 0,
                        getString(R.string.str_progress_msg_load)), params)
    }

    override fun <T> doData(data: T, id: Int) {
        super.doData(data, id)
        mList = data as List<GirlBean>
        L.e("size",mList.size.toString())
    }

}
