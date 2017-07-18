package com.yqy.gank.listener

/**
 *
 * Created by DerekYan on 2017/7/13.
 */
open interface OnRecyclerViewListener {
    fun onItemClick(position: Int)
    fun onItemLongClick(position: Int)
}