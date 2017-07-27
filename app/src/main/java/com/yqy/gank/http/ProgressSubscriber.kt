package com.yqy.gank.http

import android.content.Context

import com.yqy.gank.utils.JsonUtil
import com.yqy.gank.utils.L

import rx.Subscriber

/**
 * @author derekyan
 * *
 * @desc
 * *
 * @date 2016/12/6
 */

class ProgressSubscriber<T> : Subscriber<T>, ProgressCancelListener {
    private var mSubscriberResultListener: SubscriberResultListener<Any>? = null
    private var mProgressDialogHandler: ProgressDialogHandler? = null
    private var mContext: Context? = null
    var requestId: Int = 0
        private set //请求ID 用于识别接口

    private val cancelable = false//ProgressDialog是否可以关闭
    private var isShowDialog = true//是否显示ProgressDialog

    constructor(SubscriberResultListener: SubscriberResultListener<Any>, context: Context, requestId: Int) {
        mSubscriberResultListener = SubscriberResultListener
        mContext = context
        this.requestId = requestId
        mProgressDialogHandler = ProgressDialogHandler(context, this, cancelable)
    }

    constructor(SubscriberResultListener: SubscriberResultListener<Any>, context: Context, requestId: Int, flag: Boolean) {
        mSubscriberResultListener = SubscriberResultListener
        mContext = context
        this.requestId = requestId
        mProgressDialogHandler = ProgressDialogHandler(context, this, flag)
    }

    constructor(SubscriberResultListener: SubscriberResultListener<Any>, context: Context, requestId: Int, message: String) {
        mSubscriberResultListener = SubscriberResultListener
        mContext = context
        this.requestId = requestId
        mProgressDialogHandler = ProgressDialogHandler(context, this, cancelable, message)
    }

    constructor(SubscriberResultListener: SubscriberResultListener<Any>, context: Context, requestId: Int, message: String, showDialog: Boolean) {
        mSubscriberResultListener = SubscriberResultListener
        mContext = context
        this.requestId = requestId
        isShowDialog = showDialog
        mProgressDialogHandler = ProgressDialogHandler(context, this, cancelable, message)
    }

    /**
     * @param SubscriberResultListener
     * *
     * @param context
     * *
     * @param requestId                接口标识
     * *
     * @param flag                     是否允许关闭progressDialog
     * *
     * @param message                  progressDialog中message内容
     */
    constructor(SubscriberResultListener: SubscriberResultListener<Any>, context: Context, requestId: Int, flag: Boolean, message: String) {
        mSubscriberResultListener = SubscriberResultListener
        mContext = context
        this.requestId = requestId
        mProgressDialogHandler = ProgressDialogHandler(context, this, flag, message)
    }

    fun setShowDialog(showDialog: Boolean): ProgressSubscriber<T> {
        isShowDialog = showDialog
        return this
    }

    private fun showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler!!.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget()
        }
    }

    private fun dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler!!.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget()
            mProgressDialogHandler = null
        }
    }

    /**
     * 请求网络开始前
     */
    override fun onStart() {
        if (isShowDialog)
            showProgressDialog()
    }

    /**
     * 请求网络结束后
     */
    override fun onCompleted() {
        if (isShowDialog)
            dismissProgressDialog()
    }

    /**
     * 请求失败，响应错误，数据解析错误等，都会回调该方法

     * @param e 异常信息
     */
    override fun onError(e: Throwable) {
        if (isShowDialog)
            dismissProgressDialog()
        if (e.message?.indexOf("errorCode") != -1) {
            val errorMap = JsonUtil.jsonToMap1(e.message!!)
            if (Integer.parseInt(errorMap["errorCode"]) == 1001) {
                //                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                //                ((AbstractActivity) mContext).finish();
            } else if (mSubscriberResultListener != null)
                mSubscriberResultListener!!.onError(Integer.parseInt(errorMap["errorCode"]), errorMap["errorMsg"]!!, requestId)
        } else {
            mSubscriberResultListener!!.onError(-1, e.message!!, requestId)
        }
        if (L.isShow) {
            e.printStackTrace()
            L.e("error", e.message!!)
        }
    }

    /**
     * 对返回数据进行操作的回调， UI线程

     * @param t 返回数据bean
     */
    override fun onNext(t: T) {
        try {
            if (mSubscriberResultListener != null)
                mSubscriberResultListener!!.onNext(t!!, requestId)
        } catch (e: Exception) {
            if (L.isShow)
                e.printStackTrace()
        }

    }

    /**
     * 当cancel掉ProgressDialog的时候，能够取消订阅，也就取消了当前的Http请求
     */
    override fun onCancelProgress() {
        if (!this.isUnsubscribed) {
            this.unsubscribe()
        }
    }
}
