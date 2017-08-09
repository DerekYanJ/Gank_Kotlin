package com.yqy.gank.frame

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.yqy.gank.R
import com.yqy.gank.http.ProgressSubscriber
import com.yqy.gank.http.SubscriberResultListener
import com.yqy.gank.listener.OnAlertDialogListener
import com.yqy.gank.utils.L
import rx.Subscriber

/**
 *
 * Created by DerekYan on 2017/7/13.
 */
abstract class BaseFragment constructor() : Fragment() , View.OnClickListener, SubscriberResultListener<Any> {
    var mProgressDialog: ProgressDialog? = null
    var mAlertDialog: AlertDialog.Builder? = null
    var mContext: Context? = null
    var mView: View? = null

    //列表相关
    open var count = 10 //每页的数量
    open var pageNum: Int = 1 //页数
    open var isCanLoadMore = true //是否可以加载更多

    /** 请求的对象的结合 */
    var mSubscriberMap: MutableMap<Int, Subscriber<*>>? = HashMap()

    /**
     * 请求集合
     */
    open fun <T> addSubscriber(subscriber: ProgressSubscriber<T>): Subscriber<T>{
        if(mSubscriberMap == null) return subscriber
        //请求id
        val requestId = subscriber.requestId
        if(mSubscriberMap?.containsKey(requestId)!!){
            if(mSubscriberMap!![requestId]?.isUnsubscribed!!)
            //如果没有取消订阅 则取消订阅
                mSubscriberMap!![requestId]?.unsubscribe()
        }
        mSubscriberMap?.put(requestId, subscriber)
        return subscriber
    }

    /**
     * 清空subscriber
     */
    fun clearSubscriber(){
        if(mSubscriberMap == null) return
        try {
            for ((key, _) in mSubscriberMap!!) {
                var subscriber: Subscriber<*> = mSubscriberMap!![key]!!
                if(subscriber.isUnsubscribed)
                //取消订阅
                    subscriber.unsubscribe()
            }
        } catch(e: Exception) {
            L.e(e.printStackTrace().toString())
        }
        //非空 清空
        if(mSubscriberMap != null) mSubscriberMap?.clear()
    }

    /**
     * 请求结束后移除对应Subscriber
     */
    fun removeSubscriber(requestId: Int){
        if(mSubscriberMap == null) return
        try {
            if(mSubscriberMap?.containsKey(requestId)!!){
                if(mSubscriberMap!![requestId]?.isUnsubscribed!!)
                //取消订阅
                    mSubscriberMap!![requestId]?.unsubscribe()
            }
        } catch(e: Exception) {
            L.e(e.printStackTrace().toString())
        }
    }

    override fun onDestroy() {
        //清空
        clearSubscriber()
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater!!.inflate(preView(), container, false)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = this.activity
        initView()
        addListener()
        initData()
    }

    /**
     * 预备布局contenView id
     */
    protected abstract fun preView(): Int

    /**
     * 初始化view
     */
    protected abstract fun initView()

    /**
     * 添加监听事件
     */
    protected abstract fun addListener()

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * 设置刷新状态
     * @param mSwipeRefreshLayout
     * *
     * @param flag
     */
    fun setRefreshing(mSwipeRefreshLayout: SwipeRefreshLayout, flag: Boolean) {
        mSwipeRefreshLayout.post { mSwipeRefreshLayout.isRefreshing = flag }
    }

    open fun <T> doData(data: T, id: Int) {}
    open fun <T> doData(data: T, id: Int, qid: String) {}

    override fun onNext(t: Any, requestId: Int) {
        removeSubscriber(requestId)
        doData(t, requestId)
    }

    /**
     * 处理错误信息
     * @param errorCode
     * *
     * @param msg
     */
    open override fun onError(errorCode: Int, msg: String, requestId: Int) {
        removeSubscriber(requestId)
        if (activity != null)
            (activity as AbstractActivity).onError(errorCode, msg, requestId)
    }

    /**
     * 加载网络图片并显示到ImageView  (圆形)
     * @param url 图片地址
     * *
     * @param mImageView 要显示的ImageView
     */
    fun loadCircleImg(url: String, mImageView: ImageView) {
        (mContext as AbstractActivity).loadCircleImg(url, mImageView)
    }

    /**
     * 加载网络图片并显示到ImageView
     * @param url 图片地址
     * *
     * @param mImageView 要显示的ImageView
     */
    fun loadImg(url: String, mImageView: ImageView) {
        (mContext as AbstractActivity).loadImg(url, mImageView)
    }

    /**
     * 根布局下方提示的类似Toast 用户可滑动删除
     * @param tip 提示文字
     */
    protected fun showSnackbar(tip: String) {
        (mContext as AbstractActivity).showSnackbar(tip)
    }

    /**
     * 吐丝
     * @param tip 提示文字
     */
    protected fun showToast(tip: String) {
        Toast.makeText(mContext, tip, Toast.LENGTH_SHORT).show()
    }

    /**
     * 展示对话弹框
     * @param messageStr
     * *
     * @param cancelStr
     * *
     * @param rightStr
     * *
     * @param listener
     */
    fun showAlertDialog(messageStr: String, cancelStr: String, rightStr: String,
                        listener: OnAlertDialogListener?) {
        if (mAlertDialog == null) {
            mAlertDialog = AlertDialog.Builder(mContext!!)
        }
        mAlertDialog!!.setMessage(messageStr)
                .setNegativeButton(cancelStr) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    if (listener != null) listener!!.onNegative()
                }
                .setPositiveButton(rightStr) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    if (listener != null) listener!!.onPositive()
                }
        mAlertDialog!!.show()
    }

    /**
     * 显示加载动画

     * @param message
     */
    protected fun showProgressDialog(message: String) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this.getActivity())
            mProgressDialog!!.isIndeterminate = true
            mProgressDialog!!.setCancelable(true)
        }
        if (TextUtils.isEmpty(message))
            mProgressDialog!!.setMessage(getString(R.string.str_progress_msg_load))
        else
            mProgressDialog!!.setMessage(message)
        mProgressDialog!!.show()
    }

    /**
     * 取消加载动画
     */
    protected fun dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }

    override fun onPause() {
        // TODO Auto-generated method stub
        dismissProgressDialog()
        super.onPause()
    }

}