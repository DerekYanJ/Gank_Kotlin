package com.yqy.gank.listener

/**
 * Created by DerekYan on 2017/7/12.
 */
interface OnAlertDialogListener {
    /**
     * 右侧按钮
     */
    fun onPositive()

    /**
     * 左侧按钮
     */
    fun onNegative()
}