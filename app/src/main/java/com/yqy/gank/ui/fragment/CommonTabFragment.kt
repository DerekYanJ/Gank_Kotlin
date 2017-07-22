package com.yqy.gank.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
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
import com.yqy.gank.listener.OnRecyclerViewListener
import com.yqy.gank.ui.decoration.SpacesItemDecoration

@SuppressLint("ValidFragment")
/**
 * 公用tab
 * Created by DerekYan on 2017/7/21.
 */
class CommonTabFragment(val type: String) : BaseFragment() , SwipeRefreshLayout.OnRefreshListener {

    val swiperefreshlayout: SwipeRefreshLayout by bindView(R.id.swiperefreshlayout)
    val recyclerview: RecyclerView by bindView(R.id.recyclerview)
    var mAdapter: MyRecyclerViewAdapter<MyViewHolder>? = null
    var mList: MutableList<DataBean> = ArrayList()

    override fun preView(): Int = R.layout.fragment_common

    override fun initView() {
        swiperefreshlayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.addItemDecoration(SpacesItemDecoration(13))
        mAdapter = MyRecyclerViewAdapter(R.layout.item_common_tab, mList)
        recyclerview.adapter = mAdapter
    }

    override fun addListener() {
        swiperefreshlayout.setOnRefreshListener(this)

        /*//添加recyclerview滚动监听
        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //如果正在刷新则不处理
                if(swiperefreshlayout.isRefreshing)
                    return

                //是否可以加载跟多
                if(!isCanLoadMore){
                    return
                }

                //是否能向上滚动，false表示已经滚动到底部
                if(!recyclerview.canScrollVertically(1)){
                    //当前页数+1 开始加载更多
                    pageNum++
                    loadMore()
                }
            }
        })*/
    }

    override fun initData() {
        swiperefreshlayout.isRefreshing = true
        onRefresh()
    }

    override fun onClick(v: View?) {
    }

    override fun onRefresh() {
        mList.clear()
        req()
    }

    /**
     * 加载更多
     */
    fun loadMore(){
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

        if(0 == id){
            mList.addAll(data as List<DataBean>)
            recyclerview.adapter.notifyDataSetChanged()
        }
    }

    val mListener: OnRecyclerViewListener = object : OnRecyclerViewListener {
        override fun onItemClick(position: Int) {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val content_url = Uri.parse(mList.get(position).url)
            intent.data = content_url
            startActivity(intent)
        }

        override fun onItemLongClick(position: Int) {
        }
    }

    inner class MyRecyclerViewAdapter<VH : MyViewHolder>(layoutResId: Int, data: MutableList<DataBean>) : BaseRecyclerViewAdapter<DataBean, VH>(layoutResId, data) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
            return MyViewHolder(view!!) as VH
        }

        override fun bindData(holder: VH, data: DataBean, position: Int) {
            loadImg(data.url, holder.imageview!!)
            holder.title_textview?.text = data.desc

            if(!TextUtils.isEmpty(data.who)){
                holder.name_textview?.text = "(by ${data.who})"
            }
            holder.itemView?.setOnClickListener { mListener.onItemClick(position) }
            if(data.images.size > 0 ){
                holder.imageview?.visibility = View.VISIBLE
                loadImg(data.images[0], holder.imageview!!)
            }else holder.imageview?.visibility = View.GONE
        }
    }

    inner class MyViewHolder(view: View) : BaseRecyclerViewAdapter.BaseViewHolder(view) {
        var imageview: ImageView? = null
        var title_textview: TextView? = null
        var name_textview: TextView? = null

        init {
            imageview = mView?.findViewById(R.id.imageview) as ImageView?
            title_textview = mView?.findViewById(R.id.title_textview) as TextView?
            name_textview = mView?.findViewById(R.id.name_textview) as TextView?
        }
    }
}