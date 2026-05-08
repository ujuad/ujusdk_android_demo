package com.ujuad.demo.utils

import android.util.Log

/**
 * @CreateDate: 2026/2/4 14:32
 * @Author: 青柠
 * @Description: 用于Demo日志打印
 */
object DemoLogUtils {
    private const val UJU_AD_DEMO = "UJU_AD_DEMO"
    fun d(message: String) {
        Log.d(UJU_AD_DEMO, buildMsg(message))
    }

    fun i(message: String) {
        Log.i(UJU_AD_DEMO, buildMsg(message))
    }

    fun w(message: String) {
        Log.w(UJU_AD_DEMO, buildMsg(message))
    }

    fun e(message: String, throwable: Throwable? = null) {
        Log.e(UJU_AD_DEMO, buildMsg(message), throwable)
    }

    // --- 私有辅助逻辑 ---

    private fun buildMsg(message: String): String {
        return "${getStackTraceInfo()} $message"
    }

    private fun getStackTraceInfo(): String {
        val stackTrace = Thread.currentThread().stackTrace
        // 索引说明：0:getThreadStackTrace, 1:getStackTrace, 2:getStackTraceInfo, 3:buildMsg/publicLog, 4:真正调用点
        for (i in 4 until stackTrace.size) {
            val className = stackTrace[i].className
            if (!className.contains("LogUtils") && !className.contains("java.lang.Thread")) {
                val methodName = stackTrace[i].methodName
                val lineNumber = stackTrace[i].lineNumber
                val simpleClassName = className.substringAfterLast(".")
                return "[$simpleClassName.$methodName:$lineNumber]"
            }
        }
        return "[]"
    }
}