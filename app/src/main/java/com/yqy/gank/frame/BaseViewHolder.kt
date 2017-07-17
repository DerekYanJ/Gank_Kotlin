package com.yqy.gank.frame

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 *
 * Created by DerekYan on 2017/7/13.
 */
open class BaseViewHolder(var view: View) : RecyclerView.ViewHolder(view){
    var mView: View? = null
    init {
        mView = itemView
    }
}