package com.yqy.gank.frame

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yqy.gank.utils.L

/**
 * 基本Recycler适配器
 * Created by DerekYan on 2017/7/13.
 */

abstract class BaseRecyclerViewAdapter<D, VH : BaseRecyclerViewAdapter.BaseViewHolder>(layoutResId: Int, data: List<D>) : RecyclerView.Adapter<VH>() {

    private var layoutResId: Int = 0 //item资源Id
    private var data: List<D> = data //数据集合
    open var view: View? = null

    init {
        if (layoutResId != 0)
            this.layoutResId = layoutResId
        else
            throw NullPointerException("请设置Item资源Id")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return BaseViewHolder(view!!) as VH
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        bindData(holder, data[position])
    }

    override fun getItemCount(): Int {
        L.e("getItemCount",data.size.toString())
        return data.size
    }

    /**
     * 绑定数据
     * @param  holder
     * *
     * @param data
     */
    protected abstract fun bindData(holder: VH, data: D)

    open class BaseViewHolder : RecyclerView.ViewHolder{
        open var mView: View? = null
        constructor(view: View): super(view){
            mView = view
        }
    }

}