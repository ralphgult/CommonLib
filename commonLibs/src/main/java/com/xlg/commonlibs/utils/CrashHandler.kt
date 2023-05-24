package com.xlg.commonlibs.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import com.xlg.commonlibs.ext.app
import com.xlg.commonlibs.ext.debugLogD
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date

/**
 * 没用kotlin中的单例
 */
@Suppress("DEPRECATION")
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {
    private lateinit var mDefaultCrashHandler: Thread.UncaughtExceptionHandler
    private lateinit var mContext: Context

    companion object {
        private val PATH = "${Environment.getDataDirectory()}/log/crash/"
        private const val FILE_NAME = "crash"
        private const val FILE_NAME_SUFFIX = ".trace"

        @SuppressLint("StaticFieldLeak")
        private var sInstance = CrashHandler()

        fun getInstance(): CrashHandler = sInstance

    }

    /**
     * 初始化工作准备
     */
    fun initWork(context: Context) {
        mDefaultCrashHandler =
            Thread.getDefaultUncaughtExceptionHandler() as Thread.UncaughtExceptionHandler
        Thread.setDefaultUncaughtExceptionHandler(this)
        mContext = context
        "当前的FIle路径为：$PATH".debugLogD()
    }

    /**
     * 当APP中有未被捕获的异常，系统将会自动调用此方法
     * @param t 为出现未捕获异常的线程
     * @param e 为未捕获的异常，通过这个e就可以得到异常信息
     */
    override fun uncaughtException(t: Thread, e: Throwable) {
        try {
            //导出异常信息到SD卡中
            dumpExceptionToSDCard(e)
            "我进入了uncaughtException方法".debugLogD()
            //上传异常信息到服务器便于开发者分析日志解决bug等。。。【开发者自己处理】
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /***
     * 导出异常信息到SDCard中
     * @param e 为未捕获的异常，通过这个e就可以得到异常信息
     */
    @SuppressLint("SimpleDateFormat")
    private fun dumpExceptionToSDCard(e: Throwable?) {
        "我进入了dumpExceptionToSDCard方法".debugLogD()

        val dir = File(PATH)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val current = System.currentTimeMillis()
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(current))
        val file = File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX)
        "文件位于:${file.absolutePath}".debugLogD()
        try {
            PrintWriter(BufferedWriter(FileWriter(file))).use {
                it.println(time)
                dumpPhoneInfo(it)
                it.println()
                e!!.printStackTrace(it)
            }
        } catch (ex: Exception) {
            "导出异常信息失败".debugLogD()
            ex.printStackTrace()
        }

    }

    /**
     * 导出手机信息
     */
    private fun dumpPhoneInfo(pw: PrintWriter) {
        val pm = app.packageManager
        val pi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getPackageInfo(app.packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            pm.getPackageInfo(app.packageName, 0)
        }
        pw.print("APP版本：")
        pw.print(pi.versionName)

        pw.print("Android版本号：")
        pw.print(Build.VERSION.RELEASE)
        pw.println("_")
        pw.print(Build.VERSION.SDK_INT)

        pw.print("手机制造商(Vendor): ")
        pw.println(Build.MANUFACTURER)

        pw.print("手机型号(Model): ")
        pw.println(Build.MODEL)
    }


}