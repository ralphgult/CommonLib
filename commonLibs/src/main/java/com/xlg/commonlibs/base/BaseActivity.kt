package com.xlg.commonlibs.base

import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.xlg.commonlibs.R
import com.xlg.commonlibs.ext.add
import com.xlg.commonlibs.ext.delete
import com.xlg.commonlibs.ext.fullScreen
import com.xlg.commonlibs.ext.totalFullScreen
import com.xlg.commonlibs.net.NetState
import com.xlg.commonlibs.net.NetworkStateManager

abstract class BaseActivity : AppCompatActivity() {

    /**
     * 应用是否处于前台
     */
    protected var mIsAppForeground = false

    override fun onCreate(savedInstanceState: Bundle?) {
        add()
        //弹框状态栏显示
        super.onCreate(savedInstanceState)
        //添加当前界面上下文对象
        setStatusColorAndText()
        setContentView(getContentView())
        initViewModel()
        initViews(intent.extras)
        NetworkStateManager.instance.mNetworkStateCallback.observe(this) {
            onNetworkStateChanged(it)
        }
    }

    /**
     * 获取布局View
     */
    abstract fun getContentView(): View

    /**
     * 初始化ViewModel
     */
    abstract fun initViewModel()

    /**
     * 数据加载
     */
    abstract fun initViews(bundle: Bundle?)

    /**
     * 设置状态栏颜色[color]和状态栏文字图标是否使用黑色[isBlack]
     */
    protected fun setStatusColorAndText(color: Int? = null, isBlack: Boolean? = null) {
        //设置状态栏文字颜色, 隐藏底部导航栏
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = isBlack ?: isUseBlackText()
        controller.isAppearanceLightNavigationBars = !(isBlack ?: isUseBlackText())
        window.navigationBarColor = Color.WHITE
        if (isUseTotalFullScreenMode()) {
            window.totalFullScreen()
        } else if (isUseFullScreenMode()) {
            window.fullScreen()
            window.statusBarColor = ContextCompat.getColor(this, color ?: getStatusBarColor())
        } else {
            window.statusBarColor = ContextCompat.getColor(this, color ?: getStatusBarColor())
            WindowCompat.setDecorFitsSystemWindows(window, true)
        }
    }

    open fun getStatusBarColor() = android.R.color.white

    open fun isUseFullScreenMode() = false

    open fun isUseBlackText() = true

    open fun isUseTotalFullScreenMode() = false

    override fun onResume() {
        super.onResume()
        resume()
        if (!mIsAppForeground) {
            mIsAppForeground = true
        }
    }

    open fun resume() {}
    open fun stop() {}

    override fun onStop() {
        super.onStop()
        stop()
        mIsAppForeground = isAppOnForeground()
    }

    override fun moveTaskToBack(nonRoot: Boolean): Boolean {
        return super.moveTaskToBack(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        delete()
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(it: NetState) {}

    /**
     * 程序是否在前台运行
     */
    private fun isAppOnForeground(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // 获取Android设备中所有正在运行的App
        val appProcesses = activityManager.runningAppProcesses
        appProcesses.forEach {
            if (it.processName.equals(packageName)
                && it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            ) {
                return true
            }
        }
        return false
    }
}