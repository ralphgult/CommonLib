package com.xlg.commonlibs.utils

import android.view.ViewGroup
import com.xlg.commonlibs.base.BaseViewHolder

interface FunctionInterface {
    fun function(functionType: String, parMap: HashMap<String, Any> = HashMap()): Any
    fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder?
}

const val TYPE_SERVER_PATH = "server_path"
const val TYPE_IS_DEBUG = "is_debug"