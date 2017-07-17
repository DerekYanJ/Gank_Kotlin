package com.yqy.gank.http

/**
 * 请求结果监听
 * Created by DerekYan on 2017/7/12.
 */
interface SubscriberResultListener<T> {

    /**
     * 请求结果
     */
    fun onNext(t: T  ,requestId: Int)

    /**
     * 错误
     */
    fun onError(errorCode: Int, msg: String, requestId: Int)
}