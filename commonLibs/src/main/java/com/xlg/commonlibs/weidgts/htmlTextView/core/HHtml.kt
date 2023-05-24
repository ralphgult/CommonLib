package com.xlg.commonlibs.weidgts.htmlTextView.core

import android.text.Html
import android.text.Html.TagHandler
import android.text.Spanned
import com.xlg.commonlibs.weidgts.htmlTextView.interfase.IHtmlImageGetter
import org.ccil.cowan.tagsoup.HTMLSchema
import org.ccil.cowan.tagsoup.Parser
import org.xml.sax.SAXNotRecognizedException
import org.xml.sax.SAXNotSupportedException

/**
 * 作者: Created by AdminFun
 * 邮箱: 614484070@qq.com
 * 创建: 2018/12/25
 * 修改: 2018/12/25
 * 版本: v1.0.0
 */
object HHtml {
    fun fromHtml(
        source: String?,
        imageGetter: IHtmlImageGetter?,
        tagHandler: TagHandler?
    ): Spanned? {
        return fromHtml(source, Html.FROM_HTML_MODE_LEGACY, imageGetter, tagHandler)
    }

    fun fromHtml(
        source: String?,
        flags: Int,
        imageGetter: IHtmlImageGetter?,
        tagHandler: TagHandler?
    ): Spanned? {
        val parser = Parser()
        try {
            parser.setProperty(Parser.schemaProperty, HtmlParser.schema)
        } catch (e: SAXNotRecognizedException) {
            throw RuntimeException(e)
        } catch (e: SAXNotSupportedException) {
            throw RuntimeException(e)
        }
        val converter = HtmlToSpannedConverter(source, imageGetter, tagHandler, parser, flags)
        return converter.convert()
    }

    private object HtmlParser {
        val schema = HTMLSchema()
    }
}