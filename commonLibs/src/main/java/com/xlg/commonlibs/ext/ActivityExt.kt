package com.xlg.commonlibs.ext

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.FragmentActivity
import com.xlg.commonlibs.base.BaseActivity
import java.io.Serializable

private val activityList = ArrayList<FragmentActivity>()

fun FragmentActivity.add() {
    activityList.add(this)
}

fun FragmentActivity.delete() {
    activityList.remove(this)
}

fun getTopActivity(): FragmentActivity {
    return activityList.last()
}

fun clearAllActivity() {
    activityList.clear()
}

/**跳转到其他界面
 * [startAct]跳转发起的Activity
 * [target] 跳转目标Activity的Java类对象
 * [params] 跳转参数集合
 */
fun <T : FragmentActivity> jumpToTarget(
    startAct: FragmentActivity, target: Class<T>, params: HashMap<String, Any> = HashMap()
) {
    val intent = Intent(startAct, target)
    if (params.isNotEmpty()) {
        intent.putExtras(params.getBundleFromHashMap())
    }
    startAct.startActivity(intent)
}

/**获取启动界面的启动器
 * 需要获取下个界面关闭时的返回数据时，需要在Activity的onCreate方法调用之前调用此方法，获取启动器
 * 在[activityResult]方法中处理下个界面返回的数据
 * 跳转时，使用此方法创建的launcher进行跳转操作
 */
fun FragmentActivity.getActivityLauncher(
    activityResult: (result: ActivityResult) -> Unit
): ActivityResultLauncher<Intent> {
    val activityResultContract = object : ActivityResultContract<Intent, ActivityResult>() {
        override fun createIntent(context: Context, input: Intent): Intent = input
        override fun parseResult(resultCode: Int, intent: Intent?): ActivityResult =
            ActivityResult(resultCode, intent)
    }
    val mActivityResultCallback = ActivityResultCallback<ActivityResult> { result ->
        activityResult.invoke(result)
    }


    return registerForActivityResult(activityResultContract, mActivityResultCallback)
}

/**使用创建的Launcher跳转到对应界面的逻辑（需要返回）*/
fun <T : BaseActivity> jumpToTargetForResult(
    startAct: FragmentActivity,
    target: Class<T>,
    launcher: ActivityResultLauncher<Intent>,
    params: HashMap<String, Any> = HashMap(),
) {
    val intent = Intent(startAct, target)
    if (params.isNotEmpty()) {
        intent.putExtras(params.getBundleFromHashMap())
    }
    launcher.launch(intent)
}

/**将*/
private fun HashMap<String, Any>.getBundleFromHashMap(): Bundle {
    val bundle = Bundle()
    forEach {
        when (it.value) {
            is Float -> {
                bundle.putFloat(it.key, it.value as Float)
            }

            is Int -> {
                bundle.putInt(it.key, it.value as Int)
            }

            is String -> {
                bundle.putString(it.key, it.value as String)
            }

            is Boolean -> {
                bundle.putBoolean(it.key, it.value as Boolean)
            }

            is Serializable -> {
                bundle.putSerializable(it.key, it.value as Serializable)
            }

            is Parcelable -> {
                bundle.putParcelable(it.key, it.value as Parcelable)
            }

            else -> {
            }
        }
    }
    return bundle
}

