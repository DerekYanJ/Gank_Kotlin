package com.yqy.gank.frame

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 *
 * Created by DerekYan on 2017/7/13.
 */
open class BaseViewHolder1 : RecyclerView.ViewHolder{
    var mView: View? = null
    constructor(view: View): super(view){
        mView = view
    }
}