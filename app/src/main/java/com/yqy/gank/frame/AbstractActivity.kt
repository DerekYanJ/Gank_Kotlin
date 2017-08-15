package com.yqy.gank.frame

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.IBinder
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yqy.gank.http.ProgressSubscriber
import com.yqy.gank.http.SubscriberResultListener
import com.yqy.gank.listener.OnAlertDialogListener
import com.yqy.gank.listener.OnClickBackListener
import com.yqy.gank.utils.L
import com.yqy.gank.utils.glide.GlideCircleTransform
import rx.Subscriber

/**
 * acivity基类
 * Created by DerekYan on 2017/7/12.
 */
abstract class AbstractActivity : AppCompatActivity(), View.OnClickListener,SubscriberResultListener<Object>{
    var mProgressDialog: ProgressDialog? = null
    var mAlertDialog: AlertDialog.Builder? = null
    var mContext: Context? = null

    open var count = 10 //每页的数量
    open var pageNum: Int = 1 //页数
    var isLoadMore: Boolean = true //是否可以加载更多

    /** 请求的对象的结合 */
    var mSubscriberMap: MutableMap<Int,Subscriber<Any>>? = HashMap()

    /**
     * 请求集合
     */
    open fun addSubscriber(subscriber: ProgressSubscriber<Any>): Subscriber<Any>{
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
                var subscriber: Subscriber<Any> = mSubscriberMap!![key]!!
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

    var mOnClickBackListener: OnClickBackListener? = object : OnClickBackListener {
        override fun onClickBack() {
            finish()
        }
    }

    /** 预备布局contenView id */
    protected abstract fun preView(): Int
    /** 初始化view */
    protected abstract fun initView()
    /** 添加监听事件 */
    protected abstract fun addListener()
    /** 初始化数据 */
    protected abstract fun initData()
    /** 返回键(标题栏、虚拟键) 点击事件 */
    protected abstract fun getOnBackClickListener(): OnClickBackListener

    /**
     * 设置刷新状态
     */
    fun setRefreshing(mSwipeRefreshLayout: SwipeRefreshLayout,flag: Boolean){
        mSwipeRefreshLayout.isRefreshing = flag
    }

    /**
     * 处理返回数据
     */
    open fun <T> doData(data: T, id: Int){}
    open fun <T> doData(data: T, id: Int, qid: String){}

    override fun onNext(t: Object, requestId: Int) {
        removeSubscriber(requestId)
        doData(t,requestId)
    }

    /**
     * @param errorCode 错误码
     * @param msg 错误描述
     * @param requestId 请求id
     */
    override fun onError(errorCode: Int, msg: String, requestId: Int) {
        removeSubscriber(requestId)
        if (msg.indexOf("session") != -1 || errorCode == 1001) {
            //登录信息超时
            //            Intent intent = new Intent(this,LoginActivity.class);
            //            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            //            startActivity(intent);
        } else if (msg.indexOf("http.HttpRequest") != -1) {
            showToast("请求超时")
        } else if (msg.indexOf("HTTP 500 Internal Server Error") != -1) {
            showToast("请求异常")
        } else
            showToast(msg)
    }

    /**
     * 隐藏软键盘
     */
    open fun hideSoftInput(edittext: EditText){
        try {
            hideSoftInput(edittext.windowToken)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 隐藏软键盘
     */
    open fun hideSoftInput(binder: IBinder?){
        try {
            val inputMethodManager: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binder,0)
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 加载圆形图片
     */
    open fun loadCircleImg(url: String, mImageView: ImageView){
        Glide.with(this).load(url)
//                .placeholder(R.mipmap.ic_launcher)          //加载前图片
//                .error(R.mipmap.ic_launcher)                //加载失败图片
                .transform(GlideCircleTransform(this))      //切圆形
                .diskCacheStrategy(DiskCacheStrategy.ALL)   //缓存 全尺寸和适应缓存
                .into(mImageView)
    }

    /**
     * 加载普通图片
     */
    open fun loadImg(url: String, mImageView: ImageView){
        Glide.with(this).load(url)
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView)
    }

    /**
     * Snackbar 根布局下方提示的类似Toast 用户可滑动删除
     */
    open fun showSnackbar(tip: String){
        Snackbar.make(this.window.decorView.findViewById(android.R.id.content),
                tip,
                Snackbar.LENGTH_SHORT)
                .show()
    }

    /**
     * Toast
     */
    open fun showToast(tip: String) {
        Toast.makeText(this, tip, Toast.LENGTH_SHORT).show()
    }

    /**
     * 展示对话框
     */
    open fun showAlertDialog(messageStr: String?, //消息
                        cancelStr: String,  //关闭提示文字
                        rightStr: String,   //右侧确认提示文字
                        mListener: OnAlertDialogListener){
        if(mAlertDialog == null) mAlertDialog = AlertDialog.Builder(this)
        mAlertDialog?.setMessage(messageStr)
                ?.setPositiveButton(rightStr, DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    mListener.onNegative() })
                ?.setNegativeButton(cancelStr,DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    mListener.onNegative()
                })?.show()
    }

    /**
     * 加载进度框
     */
    open fun showProgressDialog(messageStr: String?){
        if(mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog?.isIndeterminate = true
            mProgressDialog?.setCancelable(true)
        }
        if(TextUtils.isEmpty(messageStr)) mProgressDialog?.setMessage("加载中...")
        else mProgressDialog?.setMessage(messageStr)
        if(mProgressDialog!!.isShowing)
            mProgressDialog?.dismiss()
        mProgressDialog?.show()
    }

    open fun dissmissProgressDialog(){
        if(mProgressDialog!!.isShowing)
            mProgressDialog?.dismiss()
    }

    /**
     * 拦截返回键
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //拦截系统返回键
            try {
                mOnClickBackListener = getOnBackClickListener()
                if(mOnClickBackListener != null) {
                    //触发我们自己的返回键监听
                    mOnClickBackListener?.onClickBack()
                    return true
                }
                finish()
            } catch(e: Exception) {

            }
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 倒计时相关
     */
    open var sendMsmCount = 60
    open fun startTimeDown(tv: TextView, sendMsmCount: Int){
        tv.isEnabled = false
        tv.tag = sendMsmCount
        tv.postDelayed(object : Runnable {
            override fun run() {
                var tag = tv.tag as Int
                tag--
                if (tag >= 0) {
                    tv.text = tag.toString() + "s"
                    tv.tag = tag
                    tv.postDelayed(this, 1000)
                } else if (tag >= -1) {
                    tv.text = "获取验证码"
                    tv.isEnabled = true
                } else {
                    tv.text = "获取验证码"
                }
            }
        }, 1000)
    }

}