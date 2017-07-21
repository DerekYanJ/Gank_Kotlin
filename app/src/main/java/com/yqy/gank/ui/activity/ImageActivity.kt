package com.yqy.gank.ui.activity

import android.view.View
import android.widget.ImageView
import butterknife.bindView
import com.yqy.gank.R
import com.yqy.gank.bean.DataBean
import com.yqy.gank.frame.BaseActivity
import com.yqy.gank.listener.OnClickBackListener

class ImageActivity : BaseActivity() {

    val back_iamgeview: ImageView by bindView(R.id.back_imageview)
    val imageview: ImageView by bindView(R.id.imageview)

    var mBean: DataBean? = null
    override fun preView(): Int = R.layout.activity_image

    override fun initView() {
        window.enterTransition = null
        back_iamgeview.setOnClickListener { finish() }

        mBean = intent.extras.getSerializable("bean") as DataBean

        loadImg(mBean?.url!!, imageview)
    }

    override fun addListener() {
    }

    override fun initData() {
    }

    override fun getOnBackClickListener(): OnClickBackListener = object : OnClickBackListener {
        override fun onClickBack() {
            finish()
            overridePendingTransition(0, 0)
        }
    }

    override fun onClick(v: View?) {
    }
}
