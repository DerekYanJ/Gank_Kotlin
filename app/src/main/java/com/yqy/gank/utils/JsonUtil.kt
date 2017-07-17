package com.yqy.gank.utils

import com.google.gson.*
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object JsonUtil {

    var gson: Gson? = null

    init {
        if (gson == null) {
            gson = Gson()
        }
    }

    /**
     * 将对象转换成json格式

     * @param ts
     * *
     * @return
     */
    fun objectToJson(ts: Any): String {
        var jsonStr: String? = null
        if (gson != null) {
            jsonStr = gson!!.toJson(ts)
        }
        return jsonStr!!
    }

    /**
     * 将对象转换成json格式(并自定义日期格式)

     * @param ts
     * *
     * @return
     */
    fun objectToJsonDateSerializer(ts: Any,
                                   dateformat: String): String {
        var jsonStr: String? = null
        gson = GsonBuilder()
                .registerTypeHierarchyAdapter(Date::class.java,
                        JsonSerializer<Date> { src, typeOfSrc, context ->
                            val format = SimpleDateFormat(
                                    dateformat)
                            JsonPrimitive(format.format(src))
                        }).setDateFormat(dateformat).create()
        if (gson != null) {
            jsonStr = gson!!.toJson(ts)
        }
        return jsonStr!!
    }

    /**
     * 将json格式转换成list对象

     * @param jsonStr
     * *
     * @return
     */
    fun jsonToList(jsonStr: String): List<*> {
        var objList: List<*>? = null
        if (gson != null) {
            val type = object : com.google.gson.reflect.TypeToken<List<*>>() {

            }.type
            objList = gson!!.fromJson<List<*>>(jsonStr, type)
        }
        return objList!!
    }

    fun jsonToBean(jsonStr: String, type: Type): Any {
        var obj: Any? = null
        if (gson != null) {
            try {
                obj = gson!!.fromJson<Any>(jsonStr, type)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }

        }
        return obj!!
    }

    /**
     * 将json格式转换成list对象，并准确指定类型

     * @param jsonStr
     * *
     * @param type
     * *
     * @return
     */
    fun jsonToList(jsonStr: String, type: Type): List<*> {
        var objList: List<*>? = null
        if (gson != null) {
            objList = gson!!.fromJson<List<*>>(jsonStr, type)
        }
        return objList!!
    }

    /**
     * 将json格式转换成map对象

     * @param jsonStr
     * *
     * @return
     */
    fun jsonToMap(jsonStr: String): Map<*, *>? {
        var objMap: Map<*, *>? = null
        if (gson != null) {
            val type = object : com.google.gson.reflect.TypeToken<Map<*, *>>() {

            }.type
            objMap = gson!!.fromJson<Map<*, *>>(jsonStr, type)
        }
        return objMap
    }

    /**
     * 将json格式转换成map对象

     * @param jsonStr
     * *
     * @return
     */
    fun jsonToMap1(jsonStr: String): Map<String, String> {
        var objMap: Map<String, String>? = null
        if (gson != null) {
            val type = object : com.google.gson.reflect.TypeToken<Map<String, String>>() {

            }.type
            objMap = gson!!.fromJson<Map<String, String>>(jsonStr, type)
        }
        return objMap!!
    }
    //	public static List<?> jsonToList(String jsonStr) {
    //		List<?> list = null;
    //		if (gson != null) {
    //			Type type = new com.google.gson.reflect.TypeToken<List<?>>() {
    //			}.getType();
    //			list = gson.fromJson(jsonStr, type);
    //		}
    //		return list;
    //	}

    /**
     * 将json转换成bean对象

     * @param jsonStr
     * *
     * @return
     */
    fun <T> jsonToBean(jsonStr: String, cl: Class<T>): T {
        var obj: T? = null
        if (gson != null) {
            try {
                obj = gson!!.fromJson(jsonStr, cl)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }

        }
        return obj!!
    }

    /**
     * 将json转换成bean对象

     * @param jsonStr
     * *
     * @param cl
     * *
     * @return
     */
    fun <T> jsonToBeanDateSerializer(jsonStr: String, cl: Class<T>,
                                     pattern: String): T {
        var obj: T? = null
        gson = GsonBuilder()
                .registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json, typeOfT, context ->
                    val format = SimpleDateFormat(pattern)
                    val dateStr = json.asString
                    try {
                        return@JsonDeserializer format.parse(dateStr)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                    null
                }).setDateFormat(pattern).create()
        if (gson != null) {
            obj = gson!!.fromJson(jsonStr, cl)
        }
        return obj!!
    }

    /**
     * 根据

     * @param jsonStr
     * *
     * @param key
     * *
     * @return
     */
    fun getJsonValue(jsonStr: String, key: String): Any {
        var rulsObj: Any? = null
        val rulsMap = jsonToMap(jsonStr)
        if (rulsMap != null && rulsMap.size > 0) {
            rulsObj = rulsMap[key]
        }
        return rulsObj!!
    }

}