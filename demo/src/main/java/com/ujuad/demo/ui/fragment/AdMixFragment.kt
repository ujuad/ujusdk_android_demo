package com.ujuad.demo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ujuad.demo.constant.DemoConfig
import com.ujuad.demo.databinding.FragmentAdMixBinding
import com.ujuad.demo.helper.BannerAdHelper
import com.ujuad.demo.helper.InterstitialAdHelper
import com.ujuad.demo.helper.NativeAdHelper
import com.ujuad.demo.helper.RewardAdHelper
import com.ujuad.demo.helper.SplashAdHelper
import com.ujuad.demo.listener.PrintLogger
import com.ujuad.demo.ui.adapter.AdInfoLogAdapter
import com.ujuad.demo.ui.dialog.LoadingDialog

/**
 * @CreateDate: 2025/12/19 16:37
 * @Author: 青柠
 * @Description: 聚合广告页面
 */
class AdMixFragment : Fragment(), PrintLogger {

    private var _binding: FragmentAdMixBinding? = null
    private val binding get() = _binding!!

    // 插屏 Helper
    private lateinit var interstitialHelper: InterstitialAdHelper

    // 激励 Helper
    private lateinit var rewardAdHelper: RewardAdHelper

    // 开屏 Helper
    private lateinit var splashAdHelper: SplashAdHelper

    // 原生 Helper
    private lateinit var nativeAdHelper: NativeAdHelper

    // Banner Helper
    private lateinit var bannerAdHelper: BannerAdHelper

    // 日志适配器
    private lateinit var adInfoLogAdapter: AdInfoLogAdapter


    // 加载中弹窗,延迟初始化
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdMixBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView() {
        //初始化广告 Helper
        interstitialHelper = InterstitialAdHelper(requireActivity(), this)
        rewardAdHelper = RewardAdHelper(requireActivity(), this)
        splashAdHelper = SplashAdHelper(requireActivity(), this)
        nativeAdHelper = NativeAdHelper(requireActivity(), this)
        bannerAdHelper = BannerAdHelper(requireActivity(), this)

        binding.titleBar.setTitle("聚合广告")
        initAdapter()

        //Load
        binding.btnSplashLoad.setOnClickListener {
            splashAdHelper.load()
            loadingDialog.show()
        }

        binding.btnInterstitialLoad.setOnClickListener {
            interstitialHelper.load()
            loadingDialog.show()
        }

        binding.btnRewardLoad.setOnClickListener {
            rewardAdHelper.load()
            loadingDialog.show()
        }
        binding.btnExpressLoad.setOnClickListener {
            nativeAdHelper.load(DemoConfig.FEED_EXPRESS_ID)
            loadingDialog.show()
        }

        binding.btnUnifiedLoad.setOnClickListener {
            nativeAdHelper.load(DemoConfig.FEED_UNIFIED_ID)
            loadingDialog.show()
        }

        binding.btnBannerLoad.setOnClickListener {
            bannerAdHelper.load()
            loadingDialog.show()
        }

        //Show
        binding.btnSplashShow.setOnClickListener {
            if (!splashAdHelper.isLoaded()) {
                Toast.makeText(requireActivity(), "请先加载开屏广告", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.clBase.visibility = View.GONE
            binding.flSplash.visibility = View.VISIBLE
            splashAdHelper.show(binding.flSplash)
        }

        binding.btnInterstitialShow.setOnClickListener {
            if (!interstitialHelper.isLoaded()) {
                Toast.makeText(requireActivity(), "请先加载插屏广告", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            interstitialHelper.show()
        }

        binding.btnRewardShow.setOnClickListener {
            if (!rewardAdHelper.isLoaded()) {
                Toast.makeText(requireActivity(), "请先加载激励视频广告", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            rewardAdHelper.show()
        }

        binding.btnExpressShow.setOnClickListener {
            showNative()
        }

        binding.btnUnifiedShow.setOnClickListener {
            showNative()
        }

        binding.btnBannerShow.setOnClickListener {
            if (!bannerAdHelper.isLoaded()) {
                Toast.makeText(requireActivity(), "请先加载Banner广告", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            bannerAdHelper.show(binding.flFeedGroup)
        }
    }

    private fun initAdapter() {
        adInfoLogAdapter = AdInfoLogAdapter()
        binding.rvInfo.apply {
            adapter = adInfoLogAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // 注册数据观察者
        adInfoLogAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                // 滚动到最新项
                binding.rvInfo.scrollToPosition(adInfoLogAdapter.itemCount - 1)
            }
        })
    }

    private fun showNative() {
        if (!nativeAdHelper.isLoaded()) {
            Toast.makeText(requireActivity(), "请先加载原生广告", Toast.LENGTH_SHORT).show()
            return
        }
        nativeAdHelper.show(binding.flFeedGroup)
    }

    override fun add(msg: String) {
        activity?.runOnUiThread {
            adInfoLogAdapter.add(msg)
        }

        // 监听开屏广告关闭
        if (msg.contains("Splash: onAdClosed")) {
            binding.clBase.visibility = View.VISIBLE
            binding.flSplash.visibility = View.GONE
        }

        // 监听广告加载完成,包含成功、错误、失败，关闭Loading
        if (msg.contains("onLoadSuccess") || msg.contains("onLoadError") || msg.contains("onAdError")) {
            loadingDialog.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        // 销毁广告
        interstitialHelper.destroy()
        rewardAdHelper.destroy()
        splashAdHelper.destroy()
        bannerAdHelper.destroy()
        nativeAdHelper.destroy()
    }

}