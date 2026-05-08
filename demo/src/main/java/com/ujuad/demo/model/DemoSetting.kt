package com.ujuad.demo.model

data class DemoSetting(
    var appId:String,
    var isTestMode:Boolean,
    var logLevel:Int,
    var nativeSoltId:String,
    var bannerSoltId:String,
    var interstitialSoltId:String,
    var rewardedSoltId:String,
    var splashSoltId:String,
    var videoStreamSoltId:String,
)
