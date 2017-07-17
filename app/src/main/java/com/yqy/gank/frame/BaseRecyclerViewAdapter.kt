package com.yqy.gank.frame

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yqy.gank.http.OnRecyclerViewListener
import java.util.*

/**
 * 基本Recycler适配器
 * Created by DerekYan on 2017/7/13.
 */

abstract class BaseRecyclerViewAdapter<D, VH : BaseViewHolder>(layoutResId: Int, data: List<D>?, listener: OnRecyclerViewListener) : RecyclerView.Adapter<VH>() {

    private var layoutResId: Int = 0 //item资源Id
    private var data: List<D>? = null //数据集合
    private var listener: OnRecyclerViewListener? = null

    init {
        this.listener = listener
        this.data = data ?: ArrayList<D>()
        if (layoutResId != 0)
            this.layoutResId = layoutResId
        else
            throw NullPointerException("请设置Item资源Id")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return BaseViewHolder(LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)) as VH
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        bindData(holder, data!![position])
        holder?.mView?.setOnClickListener(View.OnClickListener { if (listener != null) listener!!.onItemClick(position) })
        holder?.mView?.setOnLongClickListener(View.OnLongClickListener {
            if (listener != null) listener!!.onItemLongClick(position)
            false
        })
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    /**
     * 绑定数据
     * @param  holder
     * *
     * @param data
     */
    protected abstract fun bindData(holder: VH, data: D)
}