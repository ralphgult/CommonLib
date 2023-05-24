package com.xlg.commonlibs.ext

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Window.totalFullScreen() {
    //设置状态栏文字颜色, 隐藏底部导航栏
    //设置允许Window在危险区域绘制
    val layoutParams = attributes
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        layoutParams.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
    attributes = layoutParams
    WindowCompat.setDecorFitsSystemWindows(this, false)
    this.statusBarColor = Color.TRANSPARENT
    this.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    this.decorView.systemUiVisibility =
        (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE)
}

fun Window.fullScreen() {
    //设置允许Window在危险区域绘制
    val layoutParams = attributes
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        layoutParams.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
    attributes = layoutParams
    WindowCompat.setDecorFitsSystemWindows(this, false)
    statusBarColor = Color.TRANSPARENT
}
