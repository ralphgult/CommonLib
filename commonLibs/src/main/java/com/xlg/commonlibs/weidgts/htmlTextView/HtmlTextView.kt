package com.xlg.commonlibs.weidgts.htmlTextView

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.xlg.commonlibs.weidgts.htmlTextView.core.HHtml.fromHtml
import com.xlg.commonlibs.weidgts.htmlTextView.core.HtmlGetter
import com.xlg.commonlibs.weidgts.htmlTextView.interfase.IHtmlTagHandle

/**
 * 作者: Created by AdminFun
 * 邮箱: 614484070@qq.com
 * 创建: 2018/12/25
 * 修改: 2018/12/25
 * 版本: v1.0.0
 * 描述: 自定义TextView，实现一些自定义功能，功能详见顶部注释
 */
@SuppressLint("AppCompatCustomView")
class HtmlTextView : TextView {
    private val mTagHandle: IHtmlTagHandle? = null

    constructor(context: Context) : super(context) {
        initViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initViews(context)
    }

    private fun initViews(context: Context) {}

    /**
     * 要显示的、包含HTML标签的字符串
     */
    fun setHtml(html: String?) {
        this.text = fromHtml(html, HtmlGetter(this), mTagHandle)
    }
}