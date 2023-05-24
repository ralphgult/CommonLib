package com.xlg.commonlibs.ext

import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.xlg.commonlibs.utils.RefreshController
import com.xlg.commonlibs.utils.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private var clickable = true

fun View.click(click: (v: View) -> Unit) {
    if (clickable) {
        clickable = false
        CoroutineScope(Dispatchers.IO).launch {
            delayReset()
        }
    }
    this.setOnClickListener { click.invoke(it) }
}

fun arrayClick(vararg views: View, click: (v: View) -> Unit) {
    views.forEach {
        it.click(click)
    }
}

fun ArrayList<View>.addClicks(click: (v: View) -> Unit) {
    forEach {
        it.click(click)
    }
}

private suspend fun delayReset() {
    delay(500)
    clickable = true
}


/**********************列表刷新加载控制器**************************/

//刷新组件和绑定的控制器Map（同一时间可能有多个列表）
private val refreshBindMap = HashMap<SmartRefreshLayout, RefreshController>()

/**通过是否可以下拉刷新的标识[refreshFlag]和用于获取数据的回调[callback]创建并绑定控制器*/
fun SmartRefreshLayout.bindController(refreshFlag: Boolean, callback: (page: Int) -> Unit) {
    refreshBindMap[this] = RefreshController(this, refreshFlag, callback)
}

/**获取刷新组件对应的控制器*/
fun SmartRefreshLayout.getController(): RefreshController? {
    return refreshBindMap[this]
}

/**解绑控制器*/
fun SmartRefreshLayout.unBindController() {
    if (refreshBindMap.containsKey(this)) {
        refreshBindMap.remove(this)
    }
}

/**使用标识[flag]设置是否能够上拉加载更多*/
fun SmartRefreshLayout.setCanLoadMore(flag: Boolean) {
    finishRefresh()
    finishLoadMore()
    refreshBindMap[this]?.setEnableLoadMore(flag)
}

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setGone() {
    visibility = View.GONE
}

fun View.showOrGone(isShow: Boolean) {
    visibility = if (isShow) View.VISIBLE else View.GONE
}

fun String.toast(duration: Int = Toast.LENGTH_SHORT) {
    ToastUtils.showToast(this, duration)
}