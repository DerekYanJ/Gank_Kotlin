package com.yqy.gank.frame

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * 基本Recycler适配器
 * Created by DerekYan on 2017/7/13.
 */

abstract class BaseRecyclerViewAdapter<D, VH : BaseRecyclerViewAdapter.BaseViewHolder>(layoutResId: Int, data: MutableList<D>) : RecyclerView.Adapter<VH>() {

    open var layoutResId: Int = 0 //item资源Id
    open var data: List<D> = data //数据集合
    open var view: View? = null

    init {
        if (layoutResId != 0)
            this.layoutResId = layoutResId
        else
            throw NullPointerException("请设置Item资源Id")
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        bindData(holder, data[position],position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * 绑定数据
     * @param  holder
     * *
     * @param data
     */
    protected abstract fun bindData(holder: VH, data: D,position: Int)

    open class BaseViewHolder : RecyclerView.ViewHolder{
        open var mView: View? = null
        constructor(view: View): super(view){
            mView = view
        }
    }

}