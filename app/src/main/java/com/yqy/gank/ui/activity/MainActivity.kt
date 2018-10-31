package com.yqy.gank.ui.activity

import android.content.Intent
import android.graphics.Color
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory
import com.yqy.gank.R
import com.yqy.gank.frame.BaseActivity
import com.yqy.gank.listener.OnClickBackListener
import com.yqy.gank.ui.adapter.MyFragmentPagerAdapter
import com.yqy.gank.ui.fragment.CommonTabFragment
import com.yqy.gank.ui.fragment.GirlsFragment
import kotterknife.bindView

class MainActivity : BaseActivity(){

//    val collapsingtoolbar: CollapsingToolbarLayout by bindView(R.id.collapsingtoolbar)
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val tablayout: TabLayout by bindView(R.id.tablayout)
    val viewpager: ViewPager by bindView(R.id.viewpager)

    var fragmentList: MutableList<Fragment> = ArrayList()
    var titleList: MutableList<String> = ArrayList()
    val tabs = arrayOf("Android","iOS","前端","拓展资源","瞎推荐","App","休息视频")

    override fun preView(): Int = R.layout.activity_main

    override fun initView() {
//        collapsingtoolbar.title = "Gank For Kotlin"
//        collapsingtoolbar.setExpandedTitleColor(Color.WHITE)
//        collapsingtoolbar.setCollapsedTitleTextColor(Color.WHITE)
        //设置toolbar标题及其颜色
        toolbar.title = "Gank For Kotlin"
        toolbar.setTitleTextColor(Color.WHITE)

        //设置自定义的toolbar
        setSupportActionBar(toolbar)

        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH)

        //添加viewpager子内容
        fragmentList.add(GirlsFragment())

        //添加tablayout标题
        titleList.add("福利")

        /*for (tabName: String in tabs){
            fragmentList.add(CommonTabFragment(tabName))
            titleList.add(tabName)
        }*/

        tabs.map {
            fragmentList.add(CommonTabFragment(it))
            titleList.add(it)
        }

        //viewpager与tablayout绑定
        viewpager.adapter = MyFragmentPagerAdapter(supportFragmentManager,fragmentList,titleList)
        tablayout.setupWithViewPager(viewpager)
        tablayout.tabMode = TabLayout.MODE_SCROLLABLE
        tablayout.tabGravity = TabLayout.GRAVITY_CENTER
    }

    override fun addListener() {
    }

    override fun initData() {
    }

    override fun getOnBackClickListener(): OnClickBackListener = mOnClickBackListener!!

    override fun onClick(v: View?) {
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_about -> {
                startActivity(Intent(this@MainActivity,AboutActivity::class.java))
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
