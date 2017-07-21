package com.yqy.gank.bean

import java.io.Serializable

/**
 *
 * Created by DerekYan on 2017/7/17.
 */
class DataBean : Serializable{
    var _id = ""
    var createdAt = ""
    var desc = ""
    var publishedAt = ""
    var source = ""
    var type = ""
    var url = ""
    var used = ""
    var who = ""
    var images: MutableList<String> = ArrayList()
}