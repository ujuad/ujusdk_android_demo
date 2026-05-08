package com.ujuad.demo.helper

import android.app.Activity
import com.ujuad.demo.constant.DemoConfig
import com.ujuad.demo.listener.PrintLogger
import com.ujuad.demo.utils.DemoLogUtils
import com.ujusdk.adcore.exception.UjuException
import com.ujusdk.adcore.public.UjuAdObject
import com.ujusdk.adcore.public.config.UjuAdConfig
import com.ujusdk.adcore.public.listener.InterstitialAdObjectListener

/**
 * @CreateDate: 2026/2/4 16:35
 * @Author: 青柠
 * @Description:
 */
class InterstitialAdHelper(private val activity: Activity, private val logger: PrintLogger) {

    private var interstitialAd: UjuAdObject? = null

    fun load() {
        val adConfig = UjuAdConfig(
            placementId = DemoConfig.INTERSTITIAL_ID,
        )
        interstitialAd = UjuAdObject.getInterstitialObject(activity, adConfig)
        interstitialAd?.setAdObjectListener(object :
            InterstitialAdObjectListener {
            override fun onLoadSuccess(placementId: String) {
                //广告加载成功,此时可以展示，建议展示前判断isReady
                logger.add("Interstitial: onLoadSuccess")
            }

            override fun onLoadError(
                error: UjuException,
                placementId: String
            ) {
                //加载失败
                logger.add("Interstitial: onLoadError:${error.message}")
                DemoLogUtils.e("Interstitial:onLoadError:${error.message}")
            }

            override fun onAdShow() {
                //广告展示后获取价格
                val ecpm = interstitialAd?.getAdInfo()?.ecpm
                logger.add("Interstitial: onAdShow: ecpm:$ecpm")
            }

            override fun onAdError(
                error: UjuException,
                placementId: String
            ) {
                logger.add("Interstitial: onAdError:${error.message}")
                DemoLogUtils.e("Interstitial:onAdError:${error.message}")
            }

            override fun onAdPlayComplete() {
                logger.add("Interstitial: onAdPlayComplete")
            }

            override fun onAdClicked() {
                logger.add("Interstitial: onAdClicked")
            }

            override fun onLpClosed() {
                logger.add("Interstitial: onLpClosed")
            }

            override fun onAdClosed() {
                interstitialAd?.destroy()
                interstitialAd = null
                logger.add("Interstitial: onAdClosed")
            }
        })
        interstitialAd?.load()
        logger.add("Interstitial: load, placementId:${adConfig.placementId}")
    }

    fun show() {
        if (interstitialAd?.isReady() == true) {
            interstitialAd?.show(activity)
        } else {
            logger.add("Interstitial: 广告还未准备好")
        }
    }

    fun isLoaded(): Boolean {
        return interstitialAd != null
    }

    fun destroy() {
        interstitialAd?.destroy()
        interstitialAd = null
    }
}