package com.xlg.commonlibs.ext

import android.util.Log
import com.tencent.mmkv.BuildConfig

fun String.logE(target: String = packageName) {
    Log.e(target, this)
}

fun String.logI(target: String = packageName) {
    Log.i(target, this)
}

fun String.logD(target: String = packageName) {
    Log.d(target, this)
}

fun String.debugLogE(target: String = "DEBUG") {
    if (debug) {
        Log.e(target, this)
    }
}

fun String.debugLogI(target: String = "DEBUG") {
    if (debug) {
        Log.i(target, this)
    }
}

fun String.debugLogD(target: String = "DEBUG") {
    if (debug) {
        Log.d(target, this)
    }
}