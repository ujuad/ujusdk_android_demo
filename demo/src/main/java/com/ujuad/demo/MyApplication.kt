package com.ujuad.demo

import android.app.Application
import com.ujuad.demo.constant.DemoConfig
import com.ujuad.demo.utils.DemoLogUtils
import com.ujuad.demo.viewmodel.AppViewModel
import com.ujusdk.adcore.exception.UjuException
import com.ujusdk.adcore.public.UjuAdSdk
import com.ujusdk.adcore.public.base.BaseInitListener
import com.ujusdk.adcore.public.config.UjuAdInitConfig

class MyApplication : Application() {

    val appViewModel: AppViewModel by lazy {
        AppViewModel(this)
    }

    companion object {
        lateinit var context: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        initAdSdk()
    }

    /**
     * 初始化SDK
     */
    private fun initAdSdk() {
        val config = UjuAdInitConfig(
            appId = DemoConfig.APP_ID,
            appKey = DemoConfig.APP_KEY,
            appName = DemoConfig.APP_NAME,
            isDebug = true,
            wxAppId = "wx",
            //预置策略
            presetStrategyFileName = "xxx.json",
        )

        DemoLogUtils.d("SDK开始初始化")
        UjuAdSdk.init(this, config)
        UjuAdSdk.start(object : BaseInitListener {
            override fun onInitSuccess() {
                appViewModel.isSdkInitialized.value = true
                DemoLogUtils.d("SDK初始化成功")
            }

            override fun onInitFailed(error: UjuException) {
                appViewModel.isSdkInitialized.value = false
                DemoLogUtils.e("SDK初始化失败: ${error.message}")
            }
        })

    }
}
