package com.yqy.gank.ui.activity

import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.yqy.gank.R
import com.yqy.gank.bean.GirlBean
import com.yqy.gank.frame.BaseActivity
import com.yqy.gank.frame.BaseRecyclerViewAdapter
import com.yqy.gank.http.HttpRequest
import com.yqy.gank.http.OnRecyclerViewListener
import com.yqy.gank.http.ProgressSubscriber
import com.yqy.gank.listener.OnClickBackListener
import com.yqy.gank.ui.decoration.SpacesItemDecoration
import com.yqy.gank.utils.L

class MainActivity : BaseActivity() , OnRecyclerViewListener,SwipeRefreshLayout.OnRefreshListener{

    val collapsingtoolbar: CollapsingToolbarLayout by bindView(R.id.collapsingtoolbar)
    val swiperefreshlayout: SwipeRefreshLayout by bindView(R.id.swiperefreshlayout)
    val recyclerview: RecyclerView by bindView(R.id.recyclerview)
    var mAdapter: MyRecyclerViewAdapter<MyViewHolder>? = null

    var mList: List<GirlBean> = ArrayList()

    override fun preView(): Int = R.layout.activity_main

    override fun initView() {
        collapsingtoolbar.title = "Gank For Kotlin"

        recyclerview.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerview.addItemDecoration(SpacesItemDecoration(16))

    }

    override fun addListener() {
        swiperefreshlayout.setOnRefreshListener(this)
    }

    override fun initData() {
        swiperefreshlayout.isRefreshing = true
        req()
    }

    override fun getOnBackClickListener(): OnClickBackListener = null!!

    override fun onClick(v: View?) {
    }

    fun req() {
        val params = HashMap<String, String>()
        HttpRequest.getGirls(
                ProgressSubscriber<List<GirlBean>>(this, this, 0,
                        getString(R.string.str_progress_msg_load)), params)
    }

    override fun <T> doData(data: T, id: Int) {
        super.doData(data, id)
        swiperefreshlayout.isRefreshing = false

        mList = data as List<GirlBean>
        mAdapter = MyRecyclerViewAdapter(R.layout.item_girl,mList)
        recyclerview.adapter = mAdapter
        mAdapter!!.notifyDataSetChanged()
        L.e("size",mList.size.toString())
    }

    override fun onItemClick(position: Int) {
    }

    override fun onItemLongClick(position: Int) {
    }
    override fun onRefresh() {
        mList = ArrayList()
        req()
    }

    inner class MyRecyclerViewAdapter<VH: MyViewHolder>(layoutResId: Int, data: List<GirlBean>) : BaseRecyclerViewAdapter<GirlBean, VH>(layoutResId, data) {
        override fun bindData(holder: VH, data: GirlBean) {
            this@MainActivity.loadImg(data.url,holder.imageview!!)
            holder.textview?.text = data.who
            L.e("YQY","BIndData")
        }
    }

    inner class MyViewHolder(view: View) : BaseRecyclerViewAdapter.BaseViewHolder(view) {
        var imageview: ImageView? = null
        var textview: TextView? = null
        init {
            imageview = mView?.findViewById(R.id.imageview) as ImageView?
            textview = mView?.findViewById(R.id.textview) as TextView?
        }
    }
}
