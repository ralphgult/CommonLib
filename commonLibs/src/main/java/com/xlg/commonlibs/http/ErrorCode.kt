package com.xlg.commonlibs.http

import android.app.Application
import com.xlg.commonlibs.R
import com.xlg.commonlibs.ext.debugLogE

/**
 * 全局错误码枚举
 *
 * 因为目前不同的 API 会根据业务情况返回相同的错误码，所以需要根据 API 区分每个错误码的提示，所以做出以下处理。
 *
 * API 类型的错误码规则：
 * 比如登录接口 LOGIN，此 API 的 url 代码为 1，当 API 返回错误码 为 1000  且错误码偏移为 10000 时,对应 ErrorCode 为 11000
 *
 */
enum class ErrorCode(val code: Int, val messageResId: Int) {
    SUCCESS(200, 0),
    API_ERR_OTHER(-2, 0),
    NETWORK_ERROR(-3, 0),
    UNKNOWN_ERROR(999999, 0);

    //默认错误提示语
    val errorMessage: String
        get() {
            if (application == null) {
                "ErrorCode getMessage need init first.".debugLogE()
                return ""
            }
            //TODO 默认错误提示语
            var msg = ""
            if (messageResId > 0) {
                msg = application!!.resources.getString(
                    messageResId
                )
            }
            return msg
        }

    companion object {
        private var application: Application? = null
        fun fromCode(code: Int): ErrorCode {
            for (errorCode in values()) {
                if (errorCode.code == code) return errorCode
            }
            return UNKNOWN_ERROR
        }

        fun init(application: Application?) {
            Companion.application = application
        }
    }
}