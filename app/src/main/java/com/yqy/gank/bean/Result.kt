package com.yqy.gank.bean

/**
 * 请求结果bean
 * Created by DerekYan on 2017/7/12.
 */
class Result<T> {
    var error: Boolean = false
    var count: Int = 0
    var results: T? = null
}