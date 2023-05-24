package com.xlg.commonlibs.ext

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import com.xlg.commonlibs.base.BaseResponse
import org.json.JSONObject

/**************************   MMKV相关   *******************************/

const val KEY_TOKEN = "token"
const val KEY_USER_INFO = "userInfo"

/**使用key值[key]保存缓存[value]到MMKV中*/
fun saveCache(key: String, value: Any) {
    save(key, value)
}

/**
 * 使用key值[key]保存数据[value]到MMKV中
 * 此处保存为数据，key值会自动添加下划线开头
 */
fun saveData(key: String, value: Any) {
    val unClearKey = "_$key"
    save(unClearKey, value)
}

/**使用[key]值，获取缓存，未获取到返回默认值[default]*/
fun <T> readCache(key: String, default: T): T {
    return read(key, default)
}

/**使用[key]值，获取数据，未获取到返回默认值[default]*/
fun <T> readData(key: String, default: T): T {
    val unClearKey = "_$key"
    return read(unClearKey, default)
}

private fun save(key: String, value: Any) {
    MMKV.mmkvWithID("app")?.run {
        when (value) {
            is String -> {
                encode(key, value)
            }

            is Boolean -> {
                encode(key, value)
            }

            is Int -> {
                encode(key, value)
            }

            is Long -> {
                encode(key, value)
            }

            is Parcelable -> {
                encode(key, value)
            }

            else -> {
                throw MMKVTypeException("保存数据类型错误，只支持String,Boolean,Int,Long，若需要其他类型，请在工具类自行添加")
            }
        }
    }
}

private fun <T> read(key: String, default: T): T {
    MMKV.mmkvWithID("app")?.run {
        return when (default) {
            is String -> {
                decodeString(key, default) as T
            }

            is Boolean -> {
                decodeBool(key, default) as T
            }

            is Int -> {
                decodeInt(key, default) as T
            }

            is Long -> {
                decodeLong(key, default) as T
            }

            is Parcelable -> {
                decodeParcelable(key, default.javaClass) as T
            }

            else -> {
                throw MMKVTypeException("保存数据类型错误，只支持String,Boolean,Int,Long，若需要其他类型，请在工具类自行添加")
            }
        }
    } ?: return default
}

/**
 * 清理某项缓存的Value
 * @param key 需要清理的键
 */
fun delSingleCache(key: String) {
    MMKV.mmkvWithID("app")?.run {
        remove(key)
    }
}

/**
 * 清理某项数据的Value
 * @param key 需要清理的键
 */
fun delSingleData(key: String) {
    MMKV.mmkvWithID("app")?.run {
        remove("_$key")
    }
}

/**
 * 清除MMKV中临时数据（保存的Key值不以"_"开头）
 */
fun clearCacheKeys() {
    MMKV.mmkvWithID("app")?.run {
        val keys = allKeys()
        if (keys != null) {
            for (i in keys.indices) {
                if (!keys[i].startsWith("_")) {
                    remove(keys[i])
                }
            }
        }
    }
}

/**
 * 清空数据
 */
fun clearAllMMKV() {
    MMKV.mmkvWithID("app")?.clearAll()
}

/**
 * 获取MMKV缓存中是否有传递进来的key[key],true：存在传递进来的key值；false：不存在传递进来的key值
 */
fun hasCacheKey(key: String): Boolean = MMKV.mmkvWithID("app").containsKey(key)

/**
 * 获取MMKV数据中是否有传递进来的key[key],true：存在传递进来的key值；false：不存在传递进来的key值
 */
fun hasDataKey(key: String): Boolean = MMKV.mmkvWithID("app").containsKey("_$key")

private class MMKVTypeException(override val message: String?) : Throwable()

/**************************   网络请求返回数据解析   *******************************/
inline fun <reified T> Any.result(
    value: T,
    error: (message: String) -> Unit,
    success: (t: T) -> Unit
) {
    val gson = Gson()
    val result = gson.fromJson(gson.toJson(this), BaseResponse::class.java)
    val data = JSONObject(gson.toJson(this)).optString("data")
    if (result.isSuccess()) {
        val typeToken = object : TypeToken<T>() {}
        when (value) {
            is String -> {
                success(data.toString() as T)
            }

            else -> {
                success(gson.fromJson(data, typeToken.type))
            }
        }
    } else {
        error(result.msg)
    }

}