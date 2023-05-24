package com.xlg.commonlibs.weidgts.htmlTextView.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.text.TextUtils
import java.io.IOException
import java.io.InputStream

/**
 * 作者: Created by AdminFun
 * 邮箱: 614484070@qq.com
 * 创建: 2018/12/26
 * 修改: 2018/12/26
 * 版本: v1.0.0
 * 描述: 文件工具类
 */
object Tools {
    /**
     * 从Assets文件夹中读取图片
     *
     * @param resources  资源管理
     * @param assetsName 文件名称
     */
    fun getAssetsBitmap(resources: Resources, assetsName: String?): Bitmap? {
        if (TextUtils.isEmpty(assetsName)) {
            return null
        }
        val mAssetManager = resources.assets
        var open: InputStream? = null
        try {
            open = mAssetManager.open(assetsName!!)
            return BitmapFactory.decodeStream(open)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (open != null) {
                try {
                    open.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    /**
     * 从项目资源加载图片
     */
    fun getProjectBitmap(resources: Resources, source: String?, packName: String?): Bitmap? {
        return if (TextUtils.isEmpty(source)) {
            null
        } else try {
            val resId = resources.getIdentifier(source, "drawable", packName)
            val bitmapDrawable = resources.getDrawable(resId) as BitmapDrawable
            bitmapDrawable.bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}