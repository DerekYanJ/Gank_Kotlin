package com.yqy.gank.http

import com.yqy.gank.bean.DataBean
import com.yqy.gank.bean.Result
import retrofit2.http.*
import rx.Observable

/**
 *
 * Created by DerekYan on 2017/7/13.
 */
interface HttpService {

    /**
     *
     */
    @FormUrlEncoded
    @POST("")
    fun getResult(@FieldMap params: Map<String, String>): Observable<Result<Any>>

    /**
     * @param type 资源类型
     * @param pageNum 每页数量
     * @param type 页数
     */
    @GET("data/{type}/{count}/{pageNum}")
    fun getData(@Path("type") s: String,
                 @Path("count") count: Int,
                 @Path("pageNum") pageNum: Int ): Observable<Result<List<DataBean>>>


}