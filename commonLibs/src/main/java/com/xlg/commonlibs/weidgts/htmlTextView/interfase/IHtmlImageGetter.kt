package com.xlg.commonlibs.weidgts.htmlTextView.interfase

import android.text.Html.ImageGetter

/**
 * 作者: Created by AdminFun
 * 邮箱: 614484070@qq.com
 * 创建: 2018/12/25
 * 修改: 2018/12/25
 * 版本: v1.0.0
 */
interface IHtmlImageGetter : ImageGetter {
    companion object {
        /**
         * Assets资源
         */
        const val MODEL_ASSETS = "assets://"

        /**
         * 本地资源
         */
        const val MODEL_FILE = "file://"

        /**
         * 项目资源
         */
        const val MODEL_DRAWABLE = "drawable://"

        /**
         * 网络资源
         */
        const val MODEL_HTTP = "http://"

        /**
         * 网络资源:HTTPS
         */
        const val MODEL_HTTPS = "https://"
    }
}