package com.ujuad.demo.helper

import android.app.Activity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ujuad.demo.R
import com.ujuad.demo.listener.PrintLogger
import com.ujuad.demo.utils.DemoLogUtils
import com.ujusdk.adcore.exception.UjuException
import com.ujusdk.adcore.public.UjuAdObject
import com.ujusdk.adcore.public.binder.NativeAdViewBinder
import com.ujusdk.adcore.public.config.UjuAdConfig
import com.ujusdk.adcore.public.data.AdViewSize
import com.ujusdk.adcore.public.enums.FeedType
import com.ujusdk.adcore.public.listener.FeedAdObjectListener

/**
 * @CreateDate: 2026/2/4 16:51
 * @Author: 青柠
 * @Description:
 */
class NativeAdHelper(private val activity: Activity, private val logger: PrintLogger) {

    private var nativeAd: UjuAdObject? = null

    /**
     * 加载原生信息流广告
     * @param pId String 广告位ID，加载自渲染/模板信息流广告
     */
    fun load(pId: String) {
        val adConfig = UjuAdConfig(
            placementId = pId,
            //信息流尺寸，仅对模板渲染信息流有效，单位DP
            adViewSize = AdViewSize(width = 600, height = 200)
        )
        nativeAd = UjuAdObject.getNativeObject(activity, adConfig)
        nativeAd?.setAdObjectListener(object : FeedAdObjectListener {
            override fun onLoadSuccess(placementId: String) {
                //广告加载成功,此时可以展示，建议展示前判断isReady
                logger.add("Native: onLoadSuccess")
            }

            override fun onLoadError(
                error: UjuException,
                placementId: String
            ) {
                logger.add("Native: onLoadError")
                DemoLogUtils.d("Native: onLoadError")
            }

            override fun onAdError(
                error: UjuException,
                placementId: String
            ) {
                logger.add("Native: onAdError:message:${error.message}, code:${error.code}")
                DemoLogUtils.e("Native: onAdError:message:${error.message}, code:${error.code}")
            }

            override fun onAdShow() {
                //广告展示后获取价格
                val ecpm = nativeAd?.getAdInfo()?.ecpm
                logger.add("Native: onAdShow: ecpm:$ecpm")
            }

            override fun onAdClicked() {
                logger.add("Native: onAdClicked")
            }

            override fun onAdClosed() {
                logger.add("Native: onAdClosed")
            }

            override fun onLpClosed() {
                logger.add("Native: onLpClosed")
            }

        })
        nativeAd?.load()
        logger.add("Native: load, placementId:${adConfig.placementId}")
    }

    fun show(viewGroup: ViewGroup) {
        val adObject = nativeAd ?: run {
            logger.add("Native: 请先加载广告")
            return
        }

        if (adObject.isReady()) {
            if (adObject.getFeedType() == FeedType.EXPRESS) {
                //模板信息流
                adObject.show(activity, viewGroup)
            } else {
                //自渲染信息流，获取物料数据
                val data = adObject.getAdData()
                if (data == null) {
                    Toast.makeText(activity, "缺失物料数据", Toast.LENGTH_SHORT).show()
                    return
                }

                val adView =
                    activity.layoutInflater.inflate(R.layout.banner_feed_ad_view_layout, null)
                // 填充数据
                adView.findViewById<TextView>(R.id.tvADTitle).text = data.title
                adView.findViewById<TextView>(R.id.tvADDesc).text = data.desc
                adView.findViewById<TextView>(R.id.tvADSource).text = data.source
                adView.findViewById<TextView>(R.id.btnADCreative).text = data.callToAction


                //加载大图
                val ivAdPic = adView.findViewById<ImageView>(R.id.ivADPic)
                val ivAdSmall = adView.findViewById<ImageView>(R.id.ivADSmall)

                var imageUlr = ""
                if (data.imageUrl != null) {
                    imageUlr = data.imageUrl.toString()
                } else if (!data.imageUrlList.isNullOrEmpty()) {
                    imageUlr = data.imageUrlList?.get(0) ?: ""
                }

                //加载广告图片
                Glide.with(activity).load(imageUlr).into(ivAdPic)

                //加载小图，一般是广告商Icon
                Glide.with(activity).load(data.iconUrl).into(ivAdSmall)

                val binder = NativeAdViewBinder(
                    titleId = R.id.tvADTitle,
                    descId = R.id.tvADDesc,
                    sourceId = R.id.tvADSource,
                    imageId = R.id.ivADPic,
                    imageViews = listOf(ivAdPic),
                    mediaViewId = R.id.flVideo,
                    iconId = R.id.ivADSmall,
                    callToActionId = R.id.btnADCreative,
                    logoLayoutId = R.id.flADLogo
                )

                // 绑定点击响应
                adObject.registerViewForInteraction(
                    activity,
                    adView,
                    viewGroup,
                    binder
                )
            }
        } else {
            logger.add("Native: 广告还未准备好")
        }
    }

    fun isLoaded(): Boolean {
        return nativeAd != null
    }

    fun destroy() {
        nativeAd?.destroy()
        nativeAd = null
    }
}