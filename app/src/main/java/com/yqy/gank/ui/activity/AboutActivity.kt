package com.yqy.gank.ui.activity

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import butterknife.bindView
import com.yqy.gank.R
import com.yqy.gank.frame.BaseActivity
import com.yqy.gank.listener.OnClickBackListener


class AboutActivity : BaseActivity() {
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val github_textview: TextView by bindView(R.id.github_textview)
    val jianshu_textview: TextView by bindView(R.id.jianshu_textview)

    override fun preView(): Int = R.layout.activity_about

    override fun initView() {
    }

    override fun addListener() {
        //toolbar 返回键点击监听
        toolbar.setNavigationOnClickListener { finish() }
        github_textview.setOnClickListener(this)
        jianshu_textview.setOnClickListener(this)
    }

    override fun initData() {
    }

    override fun getOnBackClickListener(): OnClickBackListener = mOnClickBackListener!!

    override fun onClick(v: View?) {
        when(v?.id){
            github_textview.id  -> {
                val intent = Intent()
                intent.action = "android.intent.action.VIEW"
                val content_url = Uri.parse("https://github.com/DerekYanJ/Gank_Kotlin")
                intent.data = content_url
                startActivity(intent)
            }
            jianshu_textview.id -> {
                val intent = Intent()
                intent.action = "android.intent.action.VIEW"
                val content_url = Uri.parse("http://www.jianshu.com/u/98a61f82fad1")
                intent.data = content_url
                startActivity(intent)
            }
        }
    }
}
