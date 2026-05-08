package com.ujuad.demo.helper

import android.app.Activity
import android.view.ViewGroup
import com.ujuad.demo.constant.DemoConfig
import com.ujuad.demo.listener.PrintLogger
import com.ujuad.demo.utils.DemoLogUtils
import com.ujusdk.adcore.exception.UjuException
import com.ujusdk.adcore.public.UjuAdObject
import com.ujusdk.adcore.public.config.UjuAdConfig
import com.ujusdk.adcore.public.data.AdViewSize
import com.ujusdk.adcore.public.listener.FeedAdObjectListener

/**
 * @CreateDate: 2026/2/4 16:29
 * @Author: 青柠
 * @Description:
 */
class BannerAdHelper(private val activity: Activity, private val logger: PrintLogger) {

    private var bannerAd: UjuAdObject? = null

    fun load() {
        val adConfig = UjuAdConfig(
            placementId = DemoConfig.BANNER_ID,
            adViewSize = AdViewSize(width = 320, height = 100)
        )
        bannerAd = UjuAdObject.getBannerObject(activity, adConfig)
        bannerAd?.setAdObjectListener(object : FeedAdObjectListener {
            override fun onLoadSuccess(placementId: String) {
                //广告加载成功,此时可以展示，建议展示前判断isReady
                logger.add("Banner: onLoadSuccess")
            }

            override fun onLoadError(
                error: UjuException,
                placementId: String
            ) {
                logger.add("Banner: onLoadError：${error.message}")
                DemoLogUtils.e("onLoadError：${error.message}")
            }

            override fun onAdError(
                error: UjuException,
                placementId: String
            ) {
                logger.add("Banner: onAdError：${error.message}")
                DemoLogUtils.e("onAdError：${error.message}")
            }

            override fun onAdShow() {
                //广告展示后获取价格
                val ecpm = bannerAd?.getAdInfo()?.ecpm
                logger.add("Banner: onAdShow: ecpm:$ecpm")
            }

            override fun onAdClicked() {
                logger.add("Banner: onAdClicked")
            }

            override fun onAdClosed() {
                logger.add("Banner: onAdClosed")
            }

            override fun onLpClosed() {
                logger.add("Banner: onLpClosed")
            }
        })
        bannerAd?.load()
        logger.add("Banner: load, placementId:${adConfig.placementId}")
        DemoLogUtils.d("Banner: load, placementId:${adConfig.placementId}")
    }

    fun show(viewGroup: ViewGroup) {
        if (bannerAd?.isReady() == true) {
            bannerAd?.show(activity, viewGroup)
        } else {
            logger.add("Banner: 广告还未准备好")
        }
    }

    fun isLoaded(): Boolean {
        return bannerAd != null
    }

    fun destroy() {
        bannerAd?.destroy()
        bannerAd = null
    }
}