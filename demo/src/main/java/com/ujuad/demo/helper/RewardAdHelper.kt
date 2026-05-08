package com.ujuad.demo.helper

import android.app.Activity
import com.ujuad.demo.constant.DemoConfig
import com.ujuad.demo.listener.PrintLogger
import com.ujuad.demo.utils.DemoLogUtils
import com.ujusdk.adcore.exception.UjuException
import com.ujusdk.adcore.public.UjuAdObject
import com.ujusdk.adcore.public.config.UjuAdConfig
import com.ujusdk.adcore.public.listener.RewardAdObjectListener

/**
 * @CreateDate: 2026/2/4 16:15
 * @Author: 青柠
 * @Description: 激励广告
 */
class RewardAdHelper(private val activity: Activity, private val logger: PrintLogger) {
    private var rewardAd: UjuAdObject? = null

    /**
     * 加载激励广告
     */
    fun load() {
        val adConfig = UjuAdConfig(
            placementId = DemoConfig.REWARD_ID,
        )
        rewardAd = UjuAdObject.getRewardObject(activity, adConfig)
        rewardAd?.setAdObjectListener(object :
            RewardAdObjectListener {
            override fun onLoadSuccess(placementId: String) {
                //广告加载成功,此时可以展示，建议展示前判断isReady
                logger.add("Reward: onLoadSuccess")
            }

            override fun onLoadError(
                error: UjuException,
                placementId: String
            ) {
                //加载失败
                logger.add("Reward: onLoadError：${error.message}")
                DemoLogUtils.e("Reward: onLoadError:${error.message}")
            }

            override fun onAdShow() {
                //广告展示后获取价格
                val ecpm = rewardAd?.getAdInfo()?.ecpm
                logger.add("Reward: onAdShow: ecpm:$ecpm")
            }

            override fun onAdError(
                error: UjuException,
                placementId: String
            ) {
                logger.add("Reward: onAdError：${error.message}")
                DemoLogUtils.e("onAdError:${error.message}")
            }

            override fun onAdPlayComplete() {
                logger.add("Reward: onAdPlayComplete")
            }

            override fun onAdClicked() {
                logger.add("Reward: onAdClicked")
            }

            override fun onAdSkippedVideo() {
                logger.add("Reward: onAdSkippedVideo")
            }

            override fun onAdRewardArrived() {
                logger.add("Reward: onAdRewardArrived")
            }


            override fun onAdClosed() {
                rewardAd?.destroy()
                rewardAd = null
                logger.add("Reward: onAdClosed")
            }
        })
        rewardAd?.load()
        logger.add("Reward: load, placementId:${adConfig.placementId}")
    }

    /**
     * 展示激励广告
     */
    fun show() {
        if (rewardAd?.isReady() == true) {
            rewardAd?.show(activity)
        } else {
            logger.add("Reward: 广告还未准备好")
        }
    }

    fun isLoaded(): Boolean {
        return rewardAd != null
    }

    fun destroy() {
        rewardAd?.destroy()
        rewardAd = null
    }

}