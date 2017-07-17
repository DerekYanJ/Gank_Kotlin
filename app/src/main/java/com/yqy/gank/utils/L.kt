package com.yqy.gank.utils

import android.util.Log

/**
 * Log
 * Created by yanqy on 2017/4/18.
 */

 object L {

    /**
     * 是否展示，在不需要的时候设置为false
     */
    var isShow = false
    var TAG = "YQY"

    /**

     * @param tag
     * *            设置tag
     * @param msg
     */
    fun e(tag: String, msg: String) {
        if (isShow)
            Log.e(tag, msg)
    }

    /**

     * @param tag
     * *            设置tag
     * @param msg
     */
    fun d(tag: String, msg: String) {
        if (isShow)
            Log.d(tag, msg)
    }

    /**
     * 使用默认tag
     * @param msg
     */
    fun e(msg: String) {
        if (isShow)
            Log.e(TAG, msg)
    }
}
