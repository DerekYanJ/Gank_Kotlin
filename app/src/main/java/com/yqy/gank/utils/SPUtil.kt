package com.yqy.gank.utils

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.yqy.gank.App

/**

 * Created by yanqy on 2017/4/18.
 */

class SPUtil private constructor() {
    var sp: SharedPreferences
    val SP_NAME = "SPUtil"

    companion object{
        fun getInstance(): SPUtil {
            return Inner.instance
        }
    }

    private object Inner{
        val instance = SPUtil()
    }

    init {
        sp = App().getSharedPreferences(SP_NAME, MODE_PRIVATE)
    }

    fun write(key: String, value: String) {
        val editor = sp.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun write(key: String, value: Int) {
        val editor = sp.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    fun write(key: String, value: Boolean) {
        val editor = sp.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }

    /**
     * 删除key

     * @param key
     */
    fun remove(key: String) {
        val editor = sp.edit()
        editor.remove(key)
        editor.commit()
    }

    fun read(key: String, defaultValue: Int): Int {
        return sp.getInt(key, defaultValue)
    }

    fun read(key: String, defaultValue: Boolean?): Boolean {
        return sp.getBoolean(key, defaultValue!!)
    }

    fun read(key: String, defaultValue: String): String {
        return sp.getString(key, defaultValue)
    }
}
