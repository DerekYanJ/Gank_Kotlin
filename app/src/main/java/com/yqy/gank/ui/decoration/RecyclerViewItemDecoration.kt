package com.yqy.gank.ui.decoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


/**
 *
 * Created by DerekYan on 2017/7/19.
 */
/**
 * 为RecyclerView增加间距
 * 预设2列，如果是3列，则左右值不同
 */
class RecyclerViewItemDecoration(var space: Int) : RecyclerView.ItemDecoration() {
    var pos: Int = 0
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = space
        //该View在整个RecyclerView中位置。
        pos = parent.getChildAdapterPosition(view)
        //取模
        //两列的左边一列
        if (pos % 2 == 0) {
            outRect.left = space
            outRect.right = space / 2
        }
        //两列的右边一列
        if (pos % 2 == 1) {
            outRect.left = space / 2
            outRect.right = space
        }
    }
}