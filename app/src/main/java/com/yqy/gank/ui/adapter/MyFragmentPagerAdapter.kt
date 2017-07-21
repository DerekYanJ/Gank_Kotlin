package com.yqy.gank.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

/**
 * Tablayout + Viewpager Adapter
 * Created by DerekYan on 2017/7/21.
 */
class MyFragmentPagerAdapter(fm: FragmentManager,fragmentList: List<Fragment>,titleList: List<String>) : FragmentPagerAdapter(fm){
    var fragmentList: List<Fragment>? = null
    var titleList: List<String>? = null
    init {
        this.fragmentList = fragmentList
        this.titleList = titleList
    }

    override fun getItem(position: Int): Fragment = fragmentList!![position]

    override fun getCount(): Int = fragmentList?.size!!

    override fun getPageTitle(position: Int): CharSequence = titleList?.get(position)!!

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
//        super.destroyItem(container, position, `object`)
    }
}