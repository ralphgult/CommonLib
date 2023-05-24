package com.xlg.commonlibs.weidgts.htmlTextView.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.style.ImageSpan

/**
 * 作者: Created by AdminFun
 * 邮箱: 614484070@qq.com
 * 创建: 2018/12/24
 * 修改: 2018/12/24
 * 版本: v1.0.0
 * 描述: 自定义ImageSpan，实现Image在HTML中居中显示
 */
class HtmlImageSpan : ImageSpan {
    constructor(b: Bitmap) : super(b) {}
    constructor(b: Bitmap, verticalAlignment: Int) : super(b, verticalAlignment) {}
    constructor(context: Context, bitmap: Bitmap) : super(context, bitmap) {}
    constructor(context: Context, bitmap: Bitmap, verticalAlignment: Int) : super(
        context,
        bitmap,
        verticalAlignment
    ) {
    }

    constructor(drawable: Drawable) : super(drawable) {}
    constructor(drawable: Drawable, verticalAlignment: Int) : super(drawable, verticalAlignment) {}
    constructor(drawable: Drawable, source: String) : super(drawable, source) {}
    constructor(drawable: Drawable, source: String, verticalAlignment: Int) : super(
        drawable,
        source,
        verticalAlignment
    ) {
    }

    constructor(context: Context, uri: Uri) : super(context, uri) {}
    constructor(context: Context, uri: Uri, verticalAlignment: Int) : super(
        context,
        uri,
        verticalAlignment
    ) {
    }

    constructor(context: Context, resourceId: Int) : super(context, resourceId) {}
    constructor(context: Context, resourceId: Int, verticalAlignment: Int) : super(
        context,
        resourceId,
        verticalAlignment
    ) {
    }

    override fun draw(
        canvas: Canvas, text: CharSequence, start: Int, end: Int,
        x: Float, top: Int, y: Int, bottom: Int, paint: Paint
    ) {
        val b = drawable
        val fm = paint.fontMetricsInt
        val transY = (y + fm.descent + y + fm.ascent) / 2 - b.bounds.bottom / 2
        canvas.save()
        canvas.translate(x, transY.toFloat())
        b.draw(canvas)
        canvas.restore()
    }
}