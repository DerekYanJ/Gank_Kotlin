package com.yqy.gank.http

import com.yqy.gank.bean.GirlBean
import com.yqy.gank.bean.Result
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
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
     *
     */
    @GET("data/福利/10/1")
    fun getGirls(): Observable<Result<List<GirlBean>>>

}