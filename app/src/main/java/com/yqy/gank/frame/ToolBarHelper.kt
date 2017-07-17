package com.yqy.gank.frame

import android.content.Context
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.yqy.gank.R

/**
 *
 * Created by DerekYan on 2017/7/13.
 */
class ToolBarHelper {
    /*上下文，创建view的时候需要用到*/
    private var mContext: Context? = null
    /*base view*/
    private var mContentView: FrameLayout? = null
    /*用户定义的view*/
    private var mToolBar: Toolbar? = null
    /*视图构造器*/
    private var mInflater: LayoutInflater? = null
    /*
    * 两个属性
    * 1、toolbar是否悬浮在窗口之上
    * 2、toolbar的高度获取
    * */
    private val ATTRS = intArrayOf(R.attr.windowActionBarOverlay, R.attr.actionBarSize)


    constructor(context: Context, layoutId: Int) {
        this.mContext = context
        this.mInflater = LayoutInflater.from(mContext)
        /*初始化整个内容*/
        initContentView()
        /*初始化用户定义的布局*/
        initUserView(layoutId)
        /*初始化toolbar*/
        initToolBar()
    }

    private fun initContentView() {
        /*直接创建一个帧布局，作为视图容器的父容器*/
        mContentView = FrameLayout(mContext)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        mContentView!!.layoutParams = params

    }

    private fun initToolBar() {
        /*通过inflater获取toolbar的布局文件*/
        val toolbar = mInflater?.inflate(R.layout.layout_toolbar_base, mContentView)
        mToolBar = toolbar?.findViewById(R.id.toolbar) as Toolbar
    }


    private fun initUserView(id: Int) {
        val mUserView = mInflater?.inflate(id, null)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val typedArray = mContext?.theme?.obtainStyledAttributes(ATTRS)
        /*获取主题中定义的悬浮标志*/
        val overly = typedArray?.getBoolean(0, false)
        /*获取主题中定义的toolbar的高度*/
        val toolBarSize = typedArray?.getDimension(1, mContext?.resources?.getDimension(R.dimen.abc_action_bar_default_height_material)?.toInt()?.toFloat()!!)?.toInt()
        typedArray?.recycle()
        /*如果是悬浮状态，则不需要设置间距*/
        params.topMargin = if (overly!!) 0 else toolBarSize!!
        mContentView!!.addView(mUserView, params)
    }

    fun getContentView(): FrameLayout = mContentView!!

    fun getToolBar(): Toolbar = mToolBar!!
}