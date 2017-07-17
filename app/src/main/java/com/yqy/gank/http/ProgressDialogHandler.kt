package com.yqy.gank.http

import android.app.ProgressDialog
import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils

/**
 * Handler接收两个消息来控制显示Dialog还是关闭Dialog。 创建Handler的时候我们需要传入ProgressCancelListener的对象实例。
 * Created by DerekYan on 2017/7/13.
 */
class ProgressDialogHandler : Handler {

    private var pd: ProgressDialog? = null

    private var context: Context? = null
    private var cancelable: Boolean = false
    private var mProgressCancelListener: ProgressCancelListener? = null

    private var message: String = ""

    constructor(context: Context, mProgressCancelListener: ProgressCancelListener,
                cancelable: Boolean) : super() {
        this.context = context
        this.mProgressCancelListener = mProgressCancelListener
        this.cancelable = cancelable
    }

    constructor(context: Context, mProgressCancelListener: ProgressCancelListener,
                cancelable: Boolean, message: String) : super() {
        this.context = context
        this.mProgressCancelListener = mProgressCancelListener
        this.cancelable = cancelable
        this.message = message
    }

    /**
     * 初始化
     */
    private fun initProgressDialog() {
        if (pd == null) {
            pd = ProgressDialog(context)
            if (TextUtils.isEmpty(message))
                pd!!.setMessage("加载中...")
            else
                pd!!.setMessage(message)
            pd!!.setCancelable(cancelable)

            if (cancelable) {
                pd!!.setOnCancelListener { mProgressCancelListener!!.onCancelProgress() }
            }

            if (!pd!!.isShowing) {
                pd!!.show()
            }
        }
    }

    /**
     * 关闭dialog
     */
    private fun dismissProgressDialog() {
        if (pd != null) {
            pd!!.dismiss()
            pd = null
        }
    }

    /**
     * 处理msg
     * @param msg
     */
    override fun handleMessage(msg: Message) {
        when (msg.what) {
            SHOW_PROGRESS_DIALOG -> initProgressDialog()
            DISMISS_PROGRESS_DIALOG -> dismissProgressDialog()
        }
    }

     companion object {
        val SHOW_PROGRESS_DIALOG = 1
        val DISMISS_PROGRESS_DIALOG = 2
    }
}