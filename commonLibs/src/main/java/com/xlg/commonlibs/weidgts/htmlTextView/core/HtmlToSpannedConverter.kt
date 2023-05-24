package com.xlg.commonlibs.weidgts.htmlTextView.core

import android.annotation.TargetApi
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.Html.ImageGetter
import android.text.Html.TagHandler
import android.text.Layout
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AlignmentSpan
import android.text.style.BackgroundColorSpan
import android.text.style.BulletSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ParagraphStyle
import android.text.style.QuoteSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import com.xlg.commonlibs.weidgts.htmlTextView.interfase.IHtmlImageGetter
import org.ccil.cowan.tagsoup.Parser
import org.xml.sax.Attributes
import org.xml.sax.ContentHandler
import org.xml.sax.InputSource
import org.xml.sax.Locator
import org.xml.sax.SAXException
import org.xml.sax.XMLReader
import java.io.IOException
import java.io.StringReader
import java.util.regex.Pattern

/**
 * 作者: Created by AdminFun
 * 邮箱: 614484070@qq.com
 * 创建: 2018/12/25
 * 修改: 2018/12/25
 * 版本: v1.0.0
 * 描述: 重写HTMLToSpannedConverter，实现Image在HTML中居中显示
 */
class HtmlToSpannedConverter(
    private val mSource: String?, imageGetter: IHtmlImageGetter?,
    tagHandler: TagHandler?, parser: Parser, flags: Int
) : ContentHandler {
    private val mFlags: Int
    private val mReader: XMLReader
    private val mSpannableStringBuilder: SpannableStringBuilder
    private val mImageGetter: ImageGetter?
    private val mTagHandler: TagHandler?
    private val mDefaultDrawable: Drawable

    init {
        mImageGetter = imageGetter
        mTagHandler = tagHandler
        mReader = parser
        mFlags = flags
        mDefaultDrawable = ColorDrawable(Color.parseColor("#FF4040"))
        mSpannableStringBuilder = SpannableStringBuilder()
    }

    fun convert(): Spanned {
        mReader.contentHandler = this
        try {
            mReader.parse(InputSource(StringReader(mSource)))
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: SAXException) {
            throw RuntimeException(e)
        }
        val obj: Array<out ParagraphStyle>? = mSpannableStringBuilder.getSpans(
            0,
            mSpannableStringBuilder.length,
            ParagraphStyle::class.java
        )
        obj?.apply {
            for (i in indices) {
                val start = mSpannableStringBuilder.getSpanStart(get(i))
                var end = mSpannableStringBuilder.getSpanEnd(get(i))
                if (end - 2 >= 0) {
                    if (mSpannableStringBuilder[end - 1] == '\n' &&
                        mSpannableStringBuilder[end - 2] == '\n'
                    ) {
                        end--
                    }
                }
                if (end == start) {
                    mSpannableStringBuilder.removeSpan(get(i))
                } else {
                    mSpannableStringBuilder.setSpan(get(i), start, end, Spannable.SPAN_PARAGRAPH)
                }
            }
        }
        return mSpannableStringBuilder
    }

    private fun handleStartTag(tag: String, attributes: Attributes) {
        if (tag.equals("br", ignoreCase = true)) {

        } else if (tag.equals("p", ignoreCase = true)) {
            startBlockElement(mSpannableStringBuilder, attributes, marginParagraph)
            startCssStyle(mSpannableStringBuilder, attributes)
        } else if (tag.equals("ul", ignoreCase = true)) {
            startBlockElement(mSpannableStringBuilder, attributes, marginList)
        } else if (tag.equals("li", ignoreCase = true)) {
            startLi(mSpannableStringBuilder, attributes)
        } else if (tag.equals("div", ignoreCase = true)) {
            startBlockElement(mSpannableStringBuilder, attributes, marginDiv)
        } else if (tag.equals("span", ignoreCase = true)) {
            startCssStyle(mSpannableStringBuilder, attributes)
        } else if (tag.equals("strong", ignoreCase = true)) {
            start(mSpannableStringBuilder, Bold())
        } else if (tag.equals("b", ignoreCase = true)) {
            start(mSpannableStringBuilder, Bold())
        } else if (tag.equals("em", ignoreCase = true)) {
            start(mSpannableStringBuilder, Italic())
        } else if (tag.equals("cite", ignoreCase = true)) {
            start(mSpannableStringBuilder, Italic())
        } else if (tag.equals("dfn", ignoreCase = true)) {
            start(mSpannableStringBuilder, Italic())
        } else if (tag.equals("i", ignoreCase = true)) {
            start(mSpannableStringBuilder, Italic())
        } else if (tag.equals("big", ignoreCase = true)) {
            start(mSpannableStringBuilder, Big())
        } else if (tag.equals("small", ignoreCase = true)) {
            start(mSpannableStringBuilder, Small())
        } else if (tag.equals("font", ignoreCase = true)) {
            startFont(mSpannableStringBuilder, attributes)
        } else if (tag.equals("blockquote", ignoreCase = true)) {
            startBlockquote(mSpannableStringBuilder, attributes)
        } else if (tag.equals("tt", ignoreCase = true)) {
            start(mSpannableStringBuilder, Monospace())
        } else if (tag.equals("a", ignoreCase = true)) {
            startA(mSpannableStringBuilder, attributes)
        } else if (tag.equals("u", ignoreCase = true)) {
            start(mSpannableStringBuilder, Underline())
        } else if (tag.equals("del", ignoreCase = true)) {
            start(mSpannableStringBuilder, Strikethrough())
        } else if (tag.equals("s", ignoreCase = true)) {
            start(mSpannableStringBuilder, Strikethrough())
        } else if (tag.equals("strike", ignoreCase = true)) {
            start(mSpannableStringBuilder, Strikethrough())
        } else if (tag.equals("sup", ignoreCase = true)) {
            start(mSpannableStringBuilder, Super())
        } else if (tag.equals("sub", ignoreCase = true)) {
            start(mSpannableStringBuilder, Sub())
        } else if (tag.length == 2 && tag[0].lowercaseChar() == 'h' && tag[1] >= '1' && tag[1] <= '6') {
            startHeading(mSpannableStringBuilder, attributes, tag[1].code - '1'.code)
        } else if (tag.equals("img", ignoreCase = true)) {
            startImg(mSpannableStringBuilder, attributes, mImageGetter)
        } else mTagHandler?.handleTag(true, tag, mSpannableStringBuilder, mReader)
    }

    private fun handleEndTag(tag: String) {
        if (tag.equals("br", ignoreCase = true)) {
            handleBr(mSpannableStringBuilder)
        } else if (tag.equals("p", ignoreCase = true)) {
            endCssStyle(mSpannableStringBuilder)
            endBlockElement(mSpannableStringBuilder)
        } else if (tag.equals("ul", ignoreCase = true)) {
            endBlockElement(mSpannableStringBuilder)
        } else if (tag.equals("li", ignoreCase = true)) {
            endLi(mSpannableStringBuilder)
        } else if (tag.equals("div", ignoreCase = true)) {
            endBlockElement(mSpannableStringBuilder)
        } else if (tag.equals("span", ignoreCase = true)) {
            endCssStyle(mSpannableStringBuilder)
        } else if (tag.equals("strong", ignoreCase = true)) {
            end(mSpannableStringBuilder, Bold::class.java, StyleSpan(Typeface.BOLD))
        } else if (tag.equals("b", ignoreCase = true)) {
            end(mSpannableStringBuilder, Bold::class.java, StyleSpan(Typeface.BOLD))
        } else if (tag.equals("em", ignoreCase = true)) {
            end(mSpannableStringBuilder, Italic::class.java, StyleSpan(Typeface.ITALIC))
        } else if (tag.equals("cite", ignoreCase = true)) {
            end(mSpannableStringBuilder, Italic::class.java, StyleSpan(Typeface.ITALIC))
        } else if (tag.equals("dfn", ignoreCase = true)) {
            end(mSpannableStringBuilder, Italic::class.java, StyleSpan(Typeface.ITALIC))
        } else if (tag.equals("i", ignoreCase = true)) {
            end(mSpannableStringBuilder, Italic::class.java, StyleSpan(Typeface.ITALIC))
        } else if (tag.equals("big", ignoreCase = true)) {
            end(mSpannableStringBuilder, Big::class.java, RelativeSizeSpan(1.25f))
        } else if (tag.equals("small", ignoreCase = true)) {
            end(mSpannableStringBuilder, Small::class.java, RelativeSizeSpan(0.8f))
        } else if (tag.equals("font", ignoreCase = true)) {
            endFont(mSpannableStringBuilder)
        } else if (tag.equals("blockquote", ignoreCase = true)) {
            endBlockquote(mSpannableStringBuilder)
        } else if (tag.equals("tt", ignoreCase = true)) {
            end(mSpannableStringBuilder, Monospace::class.java, TypefaceSpan("monospace"))
        } else if (tag.equals("a", ignoreCase = true)) {
            endA(mSpannableStringBuilder)
        } else if (tag.equals("u", ignoreCase = true)) {
            end(mSpannableStringBuilder, Underline::class.java, UnderlineSpan())
        } else if (tag.equals("del", ignoreCase = true)) {
            end(mSpannableStringBuilder, Strikethrough::class.java, StrikethroughSpan())
        } else if (tag.equals("s", ignoreCase = true)) {
            end(mSpannableStringBuilder, Strikethrough::class.java, StrikethroughSpan())
        } else if (tag.equals("strike", ignoreCase = true)) {
            end(mSpannableStringBuilder, Strikethrough::class.java, StrikethroughSpan())
        } else if (tag.equals("sup", ignoreCase = true)) {
            end(mSpannableStringBuilder, Super::class.java, SuperscriptSpan())
        } else if (tag.equals("sub", ignoreCase = true)) {
            end(mSpannableStringBuilder, Sub::class.java, SubscriptSpan())
        } else if (tag.length == 2 && tag[0].lowercaseChar() == 'h' && tag[1] >= '1' && tag[1] <= '6') {
            endHeading(mSpannableStringBuilder)
        } else mTagHandler?.handleTag(false, tag, mSpannableStringBuilder, mReader)
    }

    @get:TargetApi(Build.VERSION_CODES.N)
    private val marginParagraph: Int
        private get() = getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)

    @get:TargetApi(Build.VERSION_CODES.N)
    private val marginHeading: Int
        private get() = getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_HEADING)

    @get:TargetApi(Build.VERSION_CODES.N)
    private val marginListItem: Int
        private get() = getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM)

    @get:TargetApi(Build.VERSION_CODES.N)
    private val marginList: Int
        private get() = getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST)

    @get:TargetApi(Build.VERSION_CODES.N)
    private val marginDiv: Int
        private get() = getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_DIV)

    @get:TargetApi(Build.VERSION_CODES.N)
    private val marginBlockquote: Int
        private get() = getMargin(Html.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE)

    private fun getMargin(flag: Int): Int {
        return if (flag and mFlags != 0) {
            1
        } else 2
    }

    private fun startLi(text: Editable, attributes: Attributes) {
        startBlockElement(text, attributes, marginListItem)
        start(text, Bullet())
        startCssStyle(text, attributes)
    }

    private fun startBlockquote(text: Editable, attributes: Attributes) {
        startBlockElement(text, attributes, marginBlockquote)
        start(text, Blockquote())
    }

    private fun startHeading(text: Editable, attributes: Attributes, level: Int) {
        startBlockElement(text, attributes, marginHeading)
        start(text, Heading(level))
    }

    private fun startCssStyle(text: Editable, attributes: Attributes) {
        val style = attributes.getValue("", "style")
        if (style != null) {
            var m = foregroundColorPattern!!.matcher(style)
            if (m.find()) {
                val c = getHtmlColor(m.group(1))
                if (c != -1) {
                    start(text, Foreground(c or -0x1000000))
                }
            }
            m = backgroundColorPattern!!.matcher(style)
            if (m.find()) {
                val c = getHtmlColor(m.group(1))
                if (c != -1) {
                    start(text, Background(c or -0x1000000))
                }
            }
            m = textDecorationPattern!!.matcher(style)
            if (m.find()) {
                val textDecoration = m.group(1)
                if (textDecoration.equals("line-through", ignoreCase = true)) {
                    start(text, Strikethrough())
                }
            }
        }
    }

    private fun startImg(text: Editable, attributes: Attributes, img: ImageGetter?) {
        val src = attributes.getValue("", "src")
        var d: Drawable? = null
        if (img != null) {
            d = img.getDrawable(src)
        }
        if (d == null) {
            d = mDefaultDrawable
            d.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
        }
        val len = text.length
        text.append("\uFFFC")
        text.setSpan(HtmlImageSpan(d, src), len, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun startFont(text: Editable, attributes: Attributes) {
        val color = attributes.getValue("", "color")
        val face = attributes.getValue("", "face")
        if (!TextUtils.isEmpty(color)) {
            val c = getHtmlColor(color)
            if (c != -1) {
                start(text, Foreground(c or -0x1000000))
            }
        }
        if (!TextUtils.isEmpty(face)) {
            start(text, Font(face))
        }
    }

    private fun getHtmlColor(color: String): Int {
        if (mFlags and Html.FROM_HTML_OPTION_USE_CSS_COLORS == Html.FROM_HTML_OPTION_USE_CSS_COLORS) {
            val i = sColorMap!![color.lowercase()]
            if (i != null) {
                return i
            }
        }
        return Color.parseColor(color)
    }

    override fun setDocumentLocator(locator: Locator) {}

    @Throws(SAXException::class)
    override fun startDocument() {
    }

    @Throws(SAXException::class)
    override fun endDocument() {
    }

    @Throws(SAXException::class)
    override fun startPrefixMapping(prefix: String, uri: String) {
    }

    @Throws(SAXException::class)
    override fun endPrefixMapping(prefix: String) {
    }

    @Throws(SAXException::class)
    override fun startElement(
        uri: String,
        localName: String,
        qName: String,
        attributes: Attributes
    ) {
        handleStartTag(localName, attributes)
    }

    @Throws(SAXException::class)
    override fun endElement(uri: String, localName: String, qName: String) {
        handleEndTag(localName)
    }

    @Throws(SAXException::class)
    override fun characters(ch: CharArray, start: Int, length: Int) {
        val sb = StringBuilder()
        for (i in 0 until length) {
            val c = ch[i + start]
            if (c == ' ' || c == '\n') {
                var pred: Char
                var len = sb.length
                if (len == 0) {
                    len = mSpannableStringBuilder.length
                    pred = if (len == 0) {
                        '\n'
                    } else {
                        mSpannableStringBuilder[len - 1]
                    }
                } else {
                    pred = sb[len - 1]
                }
                if (pred != ' ' && pred != '\n') {
                    sb.append(' ')
                }
            } else {
                sb.append(c)
            }
        }
        mSpannableStringBuilder.append(sb)
    }

    @Throws(SAXException::class)
    override fun ignorableWhitespace(ch: CharArray, start: Int, length: Int) {
    }

    @Throws(SAXException::class)
    override fun processingInstruction(target: String, data: String) {
    }

    @Throws(SAXException::class)
    override fun skippedEntity(name: String) {
    }

    private class Bold
    private class Italic
    private class Underline
    private class Strikethrough
    private class Big
    private class Small
    private class Monospace
    private class Blockquote
    private class Super
    private class Sub
    private class Bullet
    private class Font(var mFace: String)
    private class Href(val mHref: String?)
    private class Foreground(val mForegroundColor: Int)
    private class Background(val mBackgroundColor: Int)
    private class Heading(val mLevel: Int)
    private class Newline(val mNumNewlines: Int)
    private class Alignment(val mAlignment: Layout.Alignment)
    companion object {
        private val HEADING_SIZES = floatArrayOf(
            1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1f
        )
        private var sTextAlignPattern: Pattern? = null
        private var sForegroundColorPattern: Pattern? = null
        private var sBackgroundColorPattern: Pattern? = null
        private var sTextDecorationPattern: Pattern? = null
        private val sColorMap: MutableMap<String, Int>

        init {
            sColorMap = HashMap()
            sColorMap["darkgray"] = -0x565657
            sColorMap["gray"] = -0x7f7f80
            sColorMap["lightgray"] = -0x2c2c2d
            sColorMap["darkgrey"] = -0x565657
            sColorMap["grey"] = -0x7f7f80
            sColorMap["lightgrey"] = -0x2c2c2d
            sColorMap["green"] = -0xff8000
        }

        private val textAlignPattern: Pattern?
            private get() {
                if (sTextAlignPattern == null) {
                    sTextAlignPattern = Pattern.compile("(?:\\s+|\\A)text-align\\s*:\\s*(\\S*)\\b")
                }
                return sTextAlignPattern
            }
        private val foregroundColorPattern: Pattern?
            private get() {
                if (sForegroundColorPattern == null) {
                    sForegroundColorPattern = Pattern.compile("(?:\\s+|\\A)color\\s*:\\s*(\\S*)\\b")
                }
                return sForegroundColorPattern
            }
        private val backgroundColorPattern: Pattern?
            private get() {
                if (sBackgroundColorPattern == null) {
                    sBackgroundColorPattern =
                        Pattern.compile("(?:\\s+|\\A)background(?:-color)?\\s*:\\s*(\\S*)\\b")
                }
                return sBackgroundColorPattern
            }
        private val textDecorationPattern: Pattern?
            private get() {
                if (sTextDecorationPattern == null) {
                    sTextDecorationPattern =
                        Pattern.compile("(?:\\s+|\\A)text-decoration\\s*:\\s*(\\S*)\\b")
                }
                return sTextDecorationPattern
            }

        private fun appendNewlines(text: Editable, minNewline: Int) {
            val len = text.length
            if (len == 0) {
                return
            }
            var existingNewlines = 0
            var i = len - 1
            while (i >= 0 && text[i] == '\n') {
                existingNewlines++
                i--
            }
            for (j in existingNewlines until minNewline) {
                text.append("\n")
            }
        }

        private fun startBlockElement(text: Editable, attributes: Attributes, margin: Int) {
            if (margin > 0) {
                appendNewlines(text, margin)
                start(text, Newline(margin))
            }
            val style = attributes.getValue("", "style")
            if (style != null) {
                val m = textAlignPattern!!.matcher(style)
                if (m.find()) {
                    val alignment = m.group(1)
                    if (alignment.equals("start", ignoreCase = true)) {
                        start(text, Alignment(Layout.Alignment.ALIGN_NORMAL))
                    } else if (alignment.equals("center", ignoreCase = true)) {
                        start(text, Alignment(Layout.Alignment.ALIGN_CENTER))
                    } else if (alignment.equals("end", ignoreCase = true)) {
                        start(text, Alignment(Layout.Alignment.ALIGN_OPPOSITE))
                    }
                }
            }
        }

        private fun endBlockElement(text: Editable) {
            val n = getLast(text, Newline::class.java)
            if (n != null) {
                appendNewlines(text, n.mNumNewlines)
                text.removeSpan(n)
            }
            val a = getLast(text, Alignment::class.java)
            if (a != null) {
                setSpanFromMark(text, a, AlignmentSpan.Standard(a.mAlignment))
            }
        }

        private fun handleBr(text: Editable) {
            text.append('\n')
        }

        private fun endLi(text: Editable) {
            endCssStyle(text)
            endBlockElement(text)
            end(text, Bullet::class.java, BulletSpan())
        }

        private fun endBlockquote(text: Editable) {
            endBlockElement(text)
            end(text, Blockquote::class.java, QuoteSpan())
        }

        private fun endHeading(text: Editable) {
            val h = getLast(text, Heading::class.java)
            if (h != null) {
                setSpanFromMark(
                    text, h, RelativeSizeSpan(
                        HEADING_SIZES[h.mLevel]
                    ),
                    StyleSpan(Typeface.BOLD)
                )
            }
            endBlockElement(text)
        }

        private fun <T> getLast(text: Spanned, kind: Class<T>): T? {
            val objs = text.getSpans(0, text.length, kind)
            return if (objs.isEmpty()) {
                null
            } else {
                objs[objs.size - 1]
            }
        }

        private fun setSpanFromMark(text: Spannable, mark: Any, vararg spans: Any) {
            val where = text.getSpanStart(mark)
            text.removeSpan(mark)
            val len = text.length
            if (where != len) {
                for (span in spans) {
                    text.setSpan(span, where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }

        private fun start(text: Editable, mark: Any) {
            val len = text.length
            text.setSpan(mark, len, len, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }

        private fun end(text: Editable, kind: Class<*>, repl: Any) {
            val obj = getLast(text, kind)
            if (obj != null) {
                setSpanFromMark(text, obj, repl)
            }
        }

        private fun endCssStyle(text: Editable) {
            val s = getLast(text, Strikethrough::class.java)
            if (s != null) {
                setSpanFromMark(text, s, StrikethroughSpan())
            }
            val b = getLast(text, Background::class.java)
            if (b != null) {
                setSpanFromMark(text, b, BackgroundColorSpan(b.mBackgroundColor))
            }
            val f = getLast(text, Foreground::class.java)
            if (f != null) {
                setSpanFromMark(text, f, ForegroundColorSpan(f.mForegroundColor))
            }
        }

        private fun endFont(text: Editable) {
            val font = getLast(text, Font::class.java)
            if (font != null) {
                setSpanFromMark(text, font, TypefaceSpan(font.mFace))
            }
            val foreground = getLast(text, Foreground::class.java)
            if (foreground != null) {
                setSpanFromMark(text, foreground, ForegroundColorSpan(foreground.mForegroundColor))
            }
        }

        private fun startA(text: Editable, attributes: Attributes) {
            val href = attributes.getValue("", "href")
            start(text, Href(href))
        }

        private fun endA(text: Editable) {
            val h = getLast(text, Href::class.java)
            if (h != null) {
                if (h.mHref != null) {
                    setSpanFromMark(text, h, URLSpan(h.mHref))
                }
            }
        }
    }
}