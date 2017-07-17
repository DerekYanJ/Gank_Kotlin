package com.yqy.gank.utils.glide

import android.content.Context

import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.GlideModule

/**
 * @author derekyan
 * *
 * @desc
 * *
 * @date 2016/12/6
 */

class GlideModelConfig : GlideModule {

    internal var diskSize = 1024 * 1024 * 100
    internal var memorySize = Runtime.getRuntime().maxMemory().toInt() / 8  // 取1/8最大内存作为最大缓存

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // 定义缓存大小和位置
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskSize))  //内存中
        builder.setDiskCache(ExternalCacheDiskCacheFactory(context, "cache", diskSize)) //sd卡中

        // 默认内存和图片池大小
        val calculator = MemorySizeCalculator(context)
        val defaultMemoryCacheSize = calculator.memoryCacheSize // 默认内存大小
        val defaultBitmapPoolSize = calculator.bitmapPoolSize // 默认图片池大小
        builder.setMemoryCache(LruResourceCache(defaultMemoryCacheSize)) // 该两句无需设置，是默认的
        builder.setBitmapPool(LruBitmapPool(defaultBitmapPoolSize))

        // 自定义内存和图片池大小
        builder.setMemoryCache(LruResourceCache(memorySize))
        builder.setBitmapPool(LruBitmapPool(memorySize))

        // 定义图片格式
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888)
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565) // 默认
    }

    override fun registerComponents(context: Context, glide: Glide) {}
}
