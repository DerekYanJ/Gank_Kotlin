package com.yqy.gank.ui.decoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by DerekYan on 2017/7/18.
 */
class SpacesItemDecoration(var space: Int) : RecyclerView.ItemDecoration(){

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space
        }
    }
}