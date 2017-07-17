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
import com.yqy.gank.R
import com.yqy.gank.http.SubscriberResultListener
import com.yqy.gank.listener.OnAlertDialogListener
import com.yqy.gank.listener.OnClickBackListener
import com.yqy.gank.utils.glide.GlideCircleTransform

/**
 * acivity基类
 * Created by DerekYan on 2017/7/12.
 */
abstract class AbstractActivity : AppCompatActivity(), View.OnClickListener,SubscriberResultListener<Object>{
    var mProgressDialog: ProgressDialog? = null
    var mAlertDialog: AlertDialog.Builder? = null
    var mContext: Context? = null

    var pageNum: Int = 20 //分页 每页数量
    var isLoadMore: Boolean = true //是否可以加载更多

    var mOnClickBackListener: OnClickBackListener? = null

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
        doData(t,requestId)
    }

    override fun onError(errorCode: Int, msg: String, requestId: Int) {
        if (msg.indexOf("session") != -1 || requestId == 1001) {
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
    fun hideSoftInput(edittext: EditText){
        try {
            hideSoftInput(edittext.windowToken)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 隐藏软键盘
     */
    fun hideSoftInput(binder: IBinder?){
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
    fun loadCircleImg(url: String, mImageView: ImageView){
        Glide.with(this).load(url)
                .placeholder(R.mipmap.ic_launcher)          //加载前图片
                .error(R.mipmap.ic_launcher)                //加载失败图片
                .transform(GlideCircleTransform(this))      //切圆形
                .diskCacheStrategy(DiskCacheStrategy.ALL)   //缓存 全尺寸和适应缓存
                .into(mImageView)
    }

    /**
     * 加载普通图片
     */
    fun loadImg(url: String, mImageView: ImageView){
        Glide.with(this).load(url)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView)
    }

    /**
     * Snackbar 根布局下方提示的类似Toast 用户可滑动删除
     */
    fun showSnackbar(tip: String){
        Snackbar.make(this.window.decorView.findViewById(android.R.id.content),
                tip,
                Snackbar.LENGTH_SHORT)
                .show()
    }

    /**
     * Toast
     */
    fun showToast(tip: String) {
        Toast.makeText(this, tip, Toast.LENGTH_SHORT).show()
    }

    /**
     * 展示对话框
     */
    fun showAlertDialog(messageStr: String?, //消息
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
    fun showProgressDialog(messageStr: String?){
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

    fun dissmissProgressDialog(){
        if(mProgressDialog!!.isShowing)
            mProgressDialog?.dismiss()
    }

    /**
     * 拦截返回键
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //拦截系统返回键
            mOnClickBackListener = getOnBackClickListener()
            if(mOnClickBackListener != null) {
                //触发我们自己的返回键监听
                mOnClickBackListener?.onClickBack()
                return true
            }
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 倒计时相关
     */
    var sendMsmCount = 60
    fun startTimeDown(tv: TextView, sendMsmCount: Int){
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