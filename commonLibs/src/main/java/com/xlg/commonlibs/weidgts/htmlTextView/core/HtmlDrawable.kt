package com.xlg.commonlibs.weidgts.htmlTextView.core

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import java.io.InputStream

/**
 * 作者: Created by AdminFun
 * 邮箱: 614484070@qq.com
 * 创建: 2018/12/24
 * 修改: 2018/12/24
 * 版本: v1.0.0
 * 描述: 自定义BitmapDrawable，实现Image在HTML中正常显示（系统的BitmapDrawable显示在HTML中是缩小的）
 */
class HtmlDrawable : BitmapDrawable {
    constructor(bitmap: Bitmap?) : super(bitmap) {}
    constructor(res: Resources?, mBitmap: Bitmap?) : super(res, mBitmap) {}
    constructor(filepath: String?) : super(filepath) {}
    constructor(res: Resources?, filepath: String?) : super(res, filepath) {}
    constructor(`is`: InputStream?) : super(`is`) {}
    constructor(res: Resources?, `is`: InputStream?) : super(res, `is`) {}

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val mBitmap = bitmap
        if (mBitmap != null && !mBitmap.isRecycled) {
            canvas.drawBitmap(mBitmap, 0f, 0f, paint)
        }
    }
}