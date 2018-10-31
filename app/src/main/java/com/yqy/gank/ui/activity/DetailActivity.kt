package com.yqy.gank.ui.activity

import android.content.Intent
import android.graphics.Color
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.yqy.gank.R
import com.yqy.gank.bean.DataBean
import com.yqy.gank.frame.BaseActivity
import com.yqy.gank.listener.OnClickBackListener
import kotterknife.bindView

/**
 * 文章详情
 */
class DetailActivity : BaseActivity() {
    val collapsingtoolbar: CollapsingToolbarLayout by bindView(R.id.collapsingtoolbar)
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val type_textview: TextView by bindView(R.id.type_textview)
    val date_textview: TextView by bindView(R.id.date_textview)
    val source_textview: TextView by bindView(R.id.source_textview)
    val url_textview: TextView by bindView(R.id.url_textview)
    val desc_textview: TextView by bindView(R.id.desc_textview)

    var mBean: DataBean = DataBean()

    override fun preView(): Int = R.layout.activity_detail

    override fun initView() {
        mBean = intent.getSerializableExtra("bean") as DataBean

        collapsingtoolbar.title = "by ${mBean.who}"
        collapsingtoolbar.setExpandedTitleColor(Color.WHITE)
        collapsingtoolbar.setCollapsedTitleTextColor(Color.WHITE)

        type_textview.text = mBean.type
        date_textview.text = mBean.createdAt
        source_textview.text = mBean.source
        url_textview.text = mBean.url
        desc_textview.text = mBean.desc

    }

    override fun addListener() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        url_textview.setOnClickListener {
            val intent = Intent(mContext,WebViewActivity::class.java)
            intent.putExtra("url",mBean.url)
            startActivity(intent)
        }
    }

    override fun initData() {
    }

    override fun getOnBackClickListener(): OnClickBackListener = mOnClickBackListener!!

    override fun onClick(v: View?) {
    }
}
