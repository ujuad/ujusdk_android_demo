package com.ujuad.demo.helper

import android.app.Activity
import android.view.ViewGroup
import com.ujuad.demo.constant.DemoConfig
import com.ujuad.demo.listener.PrintLogger
import com.ujuad.demo.utils.DemoLogUtils
import com.ujusdk.adcore.exception.UjuException
import com.ujusdk.adcore.public.UjuAdObject
import com.ujusdk.adcore.public.config.UjuAdConfig
import com.ujusdk.adcore.public.listener.SplashAdObjectListener

/**
 * @CreateDate: 2026/2/4 16:24
 * @Author: 青柠
 * @Description:
 */
class SplashAdHelper(private val activity: Activity, private val logger: PrintLogger) {

    private var splashAd: UjuAdObject? = null

    fun load() {
        val adConfig = UjuAdConfig(
            //广告位ID，必填
            placementId = DemoConfig.SPLASH_ID,
            //场景Key，用于统计广告展示场景（可选）
            scenarioKey = DemoConfig.SCENARIO_KEY
        )
        splashAd = UjuAdObject.getSplashObject(activity, adConfig)
        splashAd?.setAdObjectListener(object : SplashAdObjectListener {
            override fun onLoadSuccess(placementId: String) {
                //广告加载成功,此时可以展示，建议展示前判断isReady
                logger.add("Splash: onLoadSuccess")
            }

            override fun onAdShow() {
                //广告展示后获取价格
                val ecpm = splashAd?.getAdInfo()?.ecpm
                logger.add("Splash: onAdShow: ecpm:$ecpm")
            }

            override fun onAdError(
                error: UjuException,
                placementId: String
            ) {
                logger.add("Splash: onAdError：${error.message}")
                DemoLogUtils.e("onAdError：${error.message}")
            }

            override fun onAdClicked() {
                logger.add("Splash: onAdClicked")
            }

            override fun onAdClosed() {
                logger.add("Splash: onAdClosed")

                splashAd?.destroy()
                splashAd = null
            }

            override fun onLoadError(
                error: UjuException,
                placementId: String
            ) {
                logger.add("Splash: onLoadError: ${error.message}")
                DemoLogUtils.e("onLoadError：${error.message}")
            }
        })

        splashAd?.load()
        logger.add("Splash: load, placementId:${adConfig.placementId}")
    }

    fun show(viewGroup: ViewGroup) {
        if (splashAd?.isReady() == true) {
            splashAd?.show(activity, viewGroup)
        } else {
            logger.add("Splash: 广告还未准备好")
        }
    }

    fun isLoaded(): Boolean {
        return splashAd != null
    }

    fun destroy() {
        splashAd?.destroy()
        splashAd = null
    }
}