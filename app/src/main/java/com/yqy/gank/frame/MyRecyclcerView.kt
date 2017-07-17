package com.yqy.gank.frame

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 *
 * Created by DerekYan on 2017/7/13.
 */
class MyRecyclcerView : RecyclerView.Adapter<MyRecyclcerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        return null
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }
}