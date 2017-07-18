package com.yqy.gank.ui.activity

import android.content.Intent
import android.graphics.Color
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.yqy.gank.R
import com.yqy.gank.bean.GirlBean
import com.yqy.gank.frame.BaseActivity
import com.yqy.gank.frame.BaseRecyclerViewAdapter
import com.yqy.gank.http.HttpRequest
import com.yqy.gank.http.ProgressSubscriber
import com.yqy.gank.listener.OnClickBackListener
import com.yqy.gank.listener.OnRecyclerViewListener
import com.yqy.gank.utils.L

class MainActivity : BaseActivity() , OnRecyclerViewListener,SwipeRefreshLayout.OnRefreshListener{

    val collapsingtoolbar: CollapsingToolbarLayout by bindView(R.id.collapsingtoolbar)
    val swiperefreshlayout: SwipeRefreshLayout by bindView(R.id.swiperefreshlayout)
    val recyclerview: RecyclerView by bindView(R.id.recyclerview)
    var mAdapter: MyRecyclerViewAdapter<MyViewHolder>? = null
    var mList: MutableList<GirlBean> = ArrayList()

    override fun preView(): Int = R.layout.activity_main

    override fun initView() {
        collapsingtoolbar.title = "Gank For Kotlin"
        collapsingtoolbar.setExpandedTitleColor(Color.WHITE)
        collapsingtoolbar.setCollapsedTitleTextColor(Color.WHITE)

        recyclerview.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
//        recyclerview.addItemDecoration(SpacesItemDecoration(8))

        mAdapter = MyRecyclerViewAdapter(R.layout.item_girl,mList)
        recyclerview.adapter = mAdapter

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

        mList.addAll(data as List<GirlBean>)

        recyclerview.adapter.notifyDataSetChanged()
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

    val mListener: MyOnRecyclerViewListener = object : MyOnRecyclerViewListener {
        override fun onItemClick(position: Int, imageview: ImageView) {
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity,
                    Pair.create(imageview,"imageview"))
            ActivityCompat.startActivity(this@MainActivity,
                    Intent(this@MainActivity, ImageActivity::class.java)
                            .putExtra("bean",mList[position]), optionsCompat.toBundle())
            //取消过渡时屏幕闪烁
//            window.exitTransition = null
        }
    }

    interface MyOnRecyclerViewListener{
        fun onItemClick(position: Int,imageview: ImageView)
    }

    inner class MyRecyclerViewAdapter<VH: MyViewHolder>(layoutResId: Int, data: MutableList<GirlBean>) : BaseRecyclerViewAdapter<GirlBean, VH>(layoutResId, data) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
            return MyViewHolder(view!!) as VH
        }

        override fun bindData(holder: VH, data: GirlBean,position: Int) {
            this@MainActivity.loadImg(data.url,holder.imageview!!)
            holder.textview?.text = data.who
            loadImg(data.url, holder.imageview!!)
            holder.imageview?.setOnClickListener { mListener.onItemClick(position, holder.imageview!!) }
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
