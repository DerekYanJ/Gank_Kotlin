package com.yqy.gank.frame

import android.os.Bundle

/**
 *
 * Created by DerekYan on 2017/7/13.
 */
abstract class BaseActivity : AbstractActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(preView())
        mContext = this
        initView()
        initData()
        addListener()
    }


}