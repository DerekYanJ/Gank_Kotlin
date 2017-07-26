package com.yqy.gank.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import butterknife.bindView
import com.yqy.gank.R
import com.yqy.gank.frame.BaseActivity
import com.yqy.gank.listener.OnClickBackListener

class WebViewActivity : BaseActivity() {
    val webview: WebView by bindView(R.id.webview)
    val progressbar: ProgressBar by bindView(R.id.progressbar)

    override fun preView(): Int = R.layout.activity_web_view

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {

        val url = intent.getStringExtra("url")

        //设置编码
        webview.settings.defaultTextEncodingName = "utf-8"

        //设置背景颜色
        webview.setBackgroundColor(Color.argb(0,0,0,0))

        //解决加载网页自动跳转浏览器问题
        webview.setWebViewClient(mWebViewClient)

        //进度条
        webview.setWebChromeClient(mWebChromeClient)

        webview.loadUrl(url)
    }

    override fun addListener() {
    }

    override fun initData() {
    }

    override fun getOnBackClickListener(): OnClickBackListener = object : OnClickBackListener {
        override fun onClickBack() {
            //可返回则返回
            if(webview.canGoBack()) webview.goBack()
            else finish()
        }
    }

    override fun onClick(v: View?) {
    }

    val mWebViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return super.shouldOverrideUrlLoading(view, url)
        }
    }
    
    val mWebChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if(progressbar.visibility == View.GONE) return
            if(newProgress != 100)
                progressbar.progress = newProgress
            else progressbar.visibility = View.GONE
        }
    }
}
