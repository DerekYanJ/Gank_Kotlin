package com.yqy.gank.ui.fragment

import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.yqy.gank.R
import com.yqy.gank.bean.DataBean
import com.yqy.gank.frame.BaseFragment
import com.yqy.gank.frame.BaseRecyclerViewAdapter
import com.yqy.gank.http.HttpRequest
import com.yqy.gank.http.ProgressSubscriber
import com.yqy.gank.ui.activity.ImageActivity
import com.yqy.gank.ui.decoration.SpacesItemDecoration
import com.yqy.gank.utils.L

/**
 * Android
 * Created by DerekYan on 2017/7/21.
 */
class AndroidFragment(type: String) : BaseFragment() , SwipeRefreshLayout.OnRefreshListener {

    val swiperefreshlayout: SwipeRefreshLayout by bindView(R.id.swiperefreshlayout)
    val recyclerview: RecyclerView by bindView(R.id.recyclerview)
    var mAdapter: MyRecyclerViewAdapter<MyViewHolder>? = null
    var mList: MutableList<DataBean> = ArrayList()

    var type: String = ""

    init {
        this.type = type
    }

    override fun preView(): Int = R.layout.fragment_common

    override fun initView() {
        swiperefreshlayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.addItemDecoration(SpacesItemDecoration(13))
        mAdapter = MyRecyclerViewAdapter(R.layout.item_android, mList)
        recyclerview.adapter = mAdapter
    }

    override fun addListener() {
        swiperefreshlayout.setOnRefreshListener(this)
    }

    override fun initData() {
        swiperefreshlayout.isRefreshing = true
        req()
    }

    override fun onClick(v: View?) {
    }

    override fun onRefresh() {
        mList = ArrayList()
        req()
    }

    fun req() {
        HttpRequest.getData(
                ProgressSubscriber<List<DataBean>>(this, mContext, 0,
                        getString(R.string.str_progress_msg_load)), type, count, pageNum)
    }

    override fun <T> doData(data: T, id: Int) {
        super.doData(data, id)
        swiperefreshlayout.isRefreshing = false

        mList.addAll(data as List<DataBean>)

        recyclerview.adapter.notifyDataSetChanged()
        L.e("size", mList.size.toString())
    }

    val mListener: MyOnRecyclerViewListener = object : MyOnRecyclerViewListener {
        override fun onItemClick(position: Int, imageview: ImageView) {
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                    Pair.create(imageview, "imageview"))
            ActivityCompat.startActivity(activity,
                    Intent(activity, ImageActivity::class.java)
                            .putExtra("bean", mList[position]), optionsCompat.toBundle())
            activity.overridePendingTransition(0, 0)
        }
    }

    interface MyOnRecyclerViewListener {
        fun onItemClick(position: Int, imageview: ImageView)
    }

    inner class MyRecyclerViewAdapter<VH : MyViewHolder>(layoutResId: Int, data: MutableList<DataBean>) : BaseRecyclerViewAdapter<DataBean, VH>(layoutResId, data) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
            return MyViewHolder(view!!) as VH
        }

        override fun bindData(holder: VH, data: DataBean, position: Int) {
            loadImg(data.url, holder.imageview!!)
            holder.title_textview?.text = data.desc
//            holder.imageview?.setOnClickListener { mListener.onItemClick(position, holder.imageview!!) }
            if(data.images.size > 0 ){
                loadImg(data.images[0], holder.imageview!!)
            }
        }
    }

    inner class MyViewHolder(view: View) : BaseRecyclerViewAdapter.BaseViewHolder(view) {
        var imageview: ImageView? = null
        var title_textview: TextView? = null

        init {
            imageview = mView?.findViewById(R.id.imageview) as ImageView?
            title_textview = mView?.findViewById(R.id.title_textview) as TextView?
        }
    }
}