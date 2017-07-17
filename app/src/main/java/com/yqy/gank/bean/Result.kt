package com.yqy.gank.bean

/**
 * 请求结果bean
 * Created by DerekYan on 2017/7/12.
 */
class Result<T> {
    var ret: Int = 0
    var msg: String = ""
    var data: T? = null
}