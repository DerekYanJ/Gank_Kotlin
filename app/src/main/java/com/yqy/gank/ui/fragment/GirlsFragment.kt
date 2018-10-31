package com.yqy.gank.ui.fragment

import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.yqy.gank.R
import com.yqy.gank.bean.DataBean
import com.yqy.gank.frame.BaseFragment
import com.yqy.gank.frame.BaseRecyclerViewAdapter
import com.yqy.gank.http.HttpRequest
import com.yqy.gank.http.ProgressSubscriber
import com.yqy.gank.ui.activity.ImageActivity
import kotterknife.bindView

/**
 * 福利
 * Created by DerekYan on 2017/7/21.
 */
class GirlsFragment : BaseFragment() , OnRefreshListener,OnLoadmoreListener{

    val refreshLayout: SmartRefreshLayout by bindView(R.id.refreshLayout)
    val recyclerview: RecyclerView by bindView(R.id.recyclerview)
    var mAdapter: MyRecyclerViewAdapter<MyViewHolder>? = null
    var mList: MutableList<DataBean> = ArrayList()

    override fun preView(): Int = R.layout.fragment_common

    override fun initView() {
        recyclerview.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mAdapter = MyRecyclerViewAdapter(R.layout.item_girl,mList)
        recyclerview.adapter = mAdapter
    }

    override fun addListener() {
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setOnLoadmoreListener(this)

        //添加recyclerview滚动监听
        /*recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //如果正在刷新则不做吹
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
        refreshLayout.autoRefresh()
    }

    override fun onClick(v: View?) {
    }

    /**
     * onRefresh事件
     */
    override fun onRefresh(refreshlayout: RefreshLayout?) {
        //清除保存的数据
        mList.clear()

        pageNum = 1

        //开始请求
        req()
    }


    /**
     * 加载更多
     */
    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        pageNum++
        req()
    }

    /**
     * 请求数据
     */
    fun req() {
        HttpRequest.getData(
                addSubscriber(ProgressSubscriber<List<DataBean>>(this, mContext!!, 0,
                        getString(R.string.str_progress_msg_load)).setShowDialog(false)),"福利",count,pageNum)
    }

    override fun <T> doData(data: T, id: Int) {
        super.doData(data, id)
        if(refreshLayout.isRefreshing) refreshLayout.finishRefresh()
        else refreshLayout.finishLoadmore()

        //处理请求结果
        if(0 == id){
            val temp: List<DataBean> = data as List<DataBean>
            mList.addAll(temp)

            //返回数据小余每页数量时 设置不能加载更多
            if(temp.size < count)
                isCanLoadMore = false

            recyclerview.adapter.notifyDataSetChanged()
        }
    }

    val mListener: MyOnRecyclerViewListener = object : MyOnRecyclerViewListener {
        override fun onItemClick(position: Int, imageview: ImageView) {
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                    Pair.create(imageview,"imageview"))
            ActivityCompat.startActivity(activity,
                    Intent(activity, ImageActivity::class.java)
                            .putExtra("bean",mList[position]), optionsCompat.toBundle())
            activity.overridePendingTransition(0, 0)
        }
    }

    interface MyOnRecyclerViewListener{
        fun onItemClick(position: Int,imageview: ImageView)
    }

    inner class MyRecyclerViewAdapter<VH: MyViewHolder>(layoutResId: Int, data: MutableList<DataBean>) : BaseRecyclerViewAdapter<DataBean, VH>(layoutResId, data) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
            return MyViewHolder(view!!) as VH
        }

        override fun bindData(holder: VH, data: DataBean, position: Int) {
            loadImg(data.url,holder.imageview!!)
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