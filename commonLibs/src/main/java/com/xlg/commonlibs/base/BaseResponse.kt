package com.xlg.commonlibs.base

import com.xlg.commonlibs.http.ErrorCode

/*
 * Copyright (C) 2018
 * 版权所有
 *
 * 功能描述： 基本返回数据类型
 * 作者：
 * 创建时间：2018/6/14
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */
class BaseResponse : BaseData() {
    var code = 0
    var msg: String = "解析异常"
    fun isSuccess(): Boolean{
        return ErrorCode.fromCode(code) == ErrorCode.SUCCESS
    }
}