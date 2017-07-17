package com.yqy.gank.frame

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.yqy.gank.R

/**
 *
 * Created by DerekYan on 2017/7/13.
 */
abstract class ToolbarActivity : AbstractActivity(){
    var mToolbar: android.support.v7.widget.Toolbar? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewWithActionBar(preView())
        mContext = this
        initView()
        addListener()
        initData()
        mOnClickBackListener = getOnBackClickListener()
    }

    protected fun setContentViewWithActionBar(layoutResID: Int) {
        val mToolBarHelper = ToolBarHelper(this, layoutResID)
        mToolbar = mToolBarHelper.getToolBar()
        setContentView(mToolBarHelper.getContentView()) /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(mToolbar) /*自定义的一些操作*/
        onCreateCustomToolBar(mToolbar!!)
        mToolbar?.setNavigationOnClickListener(View.OnClickListener {
            if (mOnClickBackListener != null) {
                mOnClickBackListener?.onClickBack()
            } else {
                finish()
            }
        })
    }

    /**
     * 设置标题
     * @param title
     */
    protected fun setToolBarCenterTitle(title: String) {
        val actionBar = supportActionBar
        actionBar?.setTitle("")
        if (mToolbar != null) {
            var view: View? = mToolbar?.findViewById(R.id.center_title)
            if (view == null) {
                view = layoutInflater.inflate(R.layout.toolbar_center_title, mToolbar, true).findViewById(R.id.center_title)
            }
            (view as TextView).text = title
        }

    }

    /**
     * 设置右侧文字
     * @param str
     */
    protected fun setToolBarRightText(str: String) {
        if (mToolbar != null) {
            val view = mToolbar?.findViewById(R.id.right_toolbar_view)
            view?.visibility = View.VISIBLE
            view?.setOnClickListener(this)
            (view as TextView).text = str
        }
    }

    fun onCreateCustomToolBar(toolbar: android.support.v7.widget.Toolbar) {
        toolbar.setContentInsetsRelative(0, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}