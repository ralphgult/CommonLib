/*
 * SPAN_INCLUSIVE_INCLUSIVE：前后都包括，在指定范围前后插入新字符，都会应用新样式
 * SPAN_EXCLUSIVE_EXCLUSIVE：前后都不包括，在指定范围前后插入新字符，两端样式无变化
 * SPAN_INCLUSIVE_EXCLUSIVE：前面包括，后面不包括
 * SPAN_EXCLUSIVE_INCLUSIVE：后面包括，前面不包括
 */
package com.xlg.commonlibs.weidgts.htmlTextView.core

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.text.TextUtils
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.xlg.commonlibs.weidgts.htmlTextView.interfase.IHtmlImageGetter
import com.xlg.commonlibs.weidgts.htmlTextView.util.Tools

/**
 * 作者: Created by AdminFun
 * 邮箱: 614484070@qq.com
 * 创建: 2018/12/24
 * 修改: 2018/12/24
 * 版本: v1.0.0
 * 描述: 自定义ImageGetter，实现HTML显示各种类型的图片。
 * 目前支持 Assets图片、本地图片、项目资源图片(仅支持drawable)、网络图片
 */
class HtmlGetter(private val mTextView: TextView) : IHtmlImageGetter {
    private val mResources: Resources = mTextView.resources
    private val mPackName: String = mTextView.context.packageName

    override fun getDrawable(source: String): Drawable? {
        var drawableSource = source
        if (TextUtils.isEmpty(drawableSource)) {
            return null
        }
        val mBitmap: Bitmap?
        if (drawableSource.startsWith(IHtmlImageGetter.MODEL_ASSETS)) {
            drawableSource = drawableSource.substring(IHtmlImageGetter.MODEL_ASSETS.length)
            mBitmap = Tools.getAssetsBitmap(mResources, drawableSource)
        } else if (drawableSource.startsWith(IHtmlImageGetter.MODEL_FILE)) {
            drawableSource = drawableSource.substring(IHtmlImageGetter.MODEL_FILE.length)
            mBitmap = BitmapFactory.decodeFile(drawableSource)
        } else if (drawableSource.startsWith(IHtmlImageGetter.MODEL_DRAWABLE)) {
            drawableSource = drawableSource.substring(IHtmlImageGetter.MODEL_DRAWABLE.length)
            mBitmap = Tools.getProjectBitmap(mResources, drawableSource, mPackName)
        } else if (drawableSource.startsWith(IHtmlImageGetter.MODEL_HTTP) || drawableSource.startsWith(
                IHtmlImageGetter.MODEL_HTTPS
            )
        ) {
            return loadImageFromHttp(drawableSource)
        } else {
            mBitmap = null
        }
        if (mBitmap != null) {
            val mDrawable = HtmlDrawable(mResources, mBitmap)
            mDrawable.setBounds(0, 0, mBitmap.width, mBitmap.height)
            return mDrawable
        }
        return null
    }

    /**
     * 获取网络资源图片
     */
    private fun loadImageFromHttp(imageUrl: String): Drawable {
        val listDrawable = LevelListDrawable()
        Glide.with(mTextView).asBitmap().load(imageUrl).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                val bitmapDrawable = BitmapDrawable(mResources, bitmap)
                listDrawable.addLevel(1, 1, bitmapDrawable)
                listDrawable.setBounds(0, 0, bitmap.width, bitmap.height)
                listDrawable.level = 1
                mTextView.invalidate()
            }

        })
        return listDrawable
    }
}