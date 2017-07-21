package com.yqy.gank.http

import com.blankj.utilcode.utils.DeviceUtils
import com.yqy.gank.App
import com.yqy.gank.bean.DataBean
import com.yqy.gank.bean.Result
import com.yqy.gank.utils.L
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 *
 * Created by DerekYan on 2017/7/13.
 */
object HttpRequest{
    private val DEFAULT_TIMEOUT = 5L//超时时间
    var mRetrofit: Retrofit? = null
    var mHttpService: HttpService? = null

    init {
        var mBuilder: OkHttpClient.Builder = OkHttpClient().newBuilder()
        //设置超时时间 单位秒
        mBuilder.connectTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS)
        if(L.isShow){
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            mBuilder.addInterceptor(loggingInterceptor)
        }

        //添加一个设置header拦截器
        mBuilder.addInterceptor(Interceptor { chain ->
            //可以设置和添加请求头数据
            val builder = chain.request().newBuilder()
            val mRequest = chain.request().newBuilder()
                    .header("User-Agent", "android/" +
                            App().VERSIONNAME + "(" +
                            DeviceUtils.getSDKVersion() + ";" +
                            DeviceUtils.getModel() + ")")
                    //                            .header("Cookie", "JSESSIONID="+ SPUtil.getInstance().read("sessionid",""))
                    .build()
            try {
                return@Interceptor chain.proceed(mRequest)
            } catch (e: IOException) {
                e.printStackTrace()
                return@Interceptor null
            }
        })
        var mOkHttpClient = mBuilder.build()
        mRetrofit = Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://gank.io/api/") //配置的基地址
                .build()
        mHttpService = mRetrofit?.create(HttpService::class.java)
    }

    /**
     * 封装切换线程
     */
    fun <T> toSubscribe(o: Observable<T>, s: Subscriber<T>){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s)
    }

    /**
     * 请求数据统一进行预处理

     * @param <T>
    </T> */
    class ResultFunc<T> : Func1<Result<T>, T> {
        override fun call(result: Result<T>): T {
            if (result.error) {
                //主动抛异常  会自动进去OnError方法
                try {
                    val errorJson = JSONObject()
                    errorJson.put("errorCode", result.error.toString() + "")
                    errorJson.put("errorMsg", result.error)
                    throw ApiException(errorJson.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                    throwException("-1", "加载失败")
                }
            }
            return result.results!!
        }
    }

    private fun throwException(errorCode: String, errorMsg: String) {
        try {
            val errorJson = JSONObject()
            errorJson.put("errorCode", errorCode)
            errorJson.put("errorMsg", errorMsg)
            throw ApiException(errorJson.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
            throwException("-1", "加载失败")
        }

    }

    /**
     * 普通的获取数据 Object可替换要的类型
     */

    fun getResult(subscriber: Subscriber<Any>, params: Map<String, String>) {
        toSubscribe(mHttpService?.getResult(params)?.map(ResultFunc<Any>())!!, subscriber)
    }

    fun getData(subscriber: Subscriber<List<DataBean>>, type: String, count: Int, pageNum: Int) {
        toSubscribe(mHttpService?.getData(type,count,pageNum)?.map(ResultFunc<List<DataBean>>())!!, subscriber)
    }

}