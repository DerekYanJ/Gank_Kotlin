package com.yqy.gank

import android.app.Application
import com.blankj.utilcode.utils.AppUtils

/**
 *
 * Created by DerekYan on 2017/7/13.
 */
class App : Application() {
    var VERSIONNAME = ""

    override fun onCreate() {
        super.onCreate()
        VERSIONNAME = AppUtils.getAppVersionName(this)
    }
}