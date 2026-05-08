package com.ujuad.demo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ujuad.demo.constant.DemoConfig
import com.ujuad.demo.databinding.FragmentAdGroupListBinding
import com.ujuad.demo.ext.bundleOf
import com.ujuad.demo.ext.navigateFragment
import com.ujuad.demo.ui.adapter.AdGroupAdapter
import com.ujuad.demo.ui.adapter.AdSlotItem

/**
 * @CreateDate: 2025/12/16 10:58
 * @Author: 青柠
 * @Description: 广告位集合列表
 */
class AdGroupListFragment : Fragment() {

    private var _binding: FragmentAdGroupListBinding? = null
    private val binding get() = _binding!!

    // 在 AdGroupFragment 中添加
    private lateinit var splashAdapter: AdGroupAdapter
    private lateinit var interstitialAdapter: AdGroupAdapter
    private lateinit var bannerAdapter: AdGroupAdapter
    private lateinit var nativeAdapter: AdGroupAdapter
    private lateinit var rewardAdapter: AdGroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdGroupListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleBar.setTitle("广告位列表")
        initAdapters()
        setupRecyclerViews()
        initAdGroupListData()
    }

    private fun initAdapters() {
        splashAdapter = AdGroupAdapter().apply {
            setOnItemClickListener { item ->
                navigateFragment(
                    AdGroupDetailFragment(),
                    bundleOf("placementId" to item.placementId, "title" to "开屏广告")
                )
            }
        }
        interstitialAdapter = AdGroupAdapter().apply {
            setOnItemClickListener { item ->
                navigateFragment(
                    AdGroupDetailFragment(),
                    bundleOf("placementId" to item.placementId, "title" to "插屏广告")
                )
            }
        }
        bannerAdapter = AdGroupAdapter().apply {
            setOnItemClickListener { item ->
                navigateFragment(
                    AdGroupDetailFragment(),
                    bundleOf("placementId" to item.placementId, "title" to "Banner广告")
                )
            }
        }
        nativeAdapter = AdGroupAdapter().apply {
            setOnItemClickListener { item ->
                navigateFragment(
                    AdGroupDetailFragment(),
                    bundleOf("placementId" to item.placementId, "title" to "原生广告")
                )
            }
        }
        rewardAdapter = AdGroupAdapter().apply {
            setOnItemClickListener { item ->
                navigateFragment(
                    AdGroupDetailFragment(),
                    bundleOf("placementId" to item.placementId, "title" to "激励广告")
                )
            }
        }
    }

    private fun setupRecyclerViews() {
        binding.rvSplash.apply {
            adapter = splashAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.rvInterstitial.apply {
            adapter = interstitialAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.rvBanner.apply {
            adapter = bannerAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.rvOriginal.apply {
            adapter = nativeAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.rvReward.apply {
            adapter = rewardAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initAdGroupListData() {
        // 提交数据到各个adapter（实际应该根据adType分类）
        splashAdapter.submitList(listOf(AdSlotItem(DemoConfig.SPLASH_ID)))
        interstitialAdapter.submitList(listOf(AdSlotItem(DemoConfig.INTERSTITIAL_ID)))
        bannerAdapter.submitList(listOf(AdSlotItem(DemoConfig.BANNER_ID)))
        nativeAdapter.submitList(
            listOf(
                AdSlotItem(DemoConfig.FEED_EXPRESS_ID),
                AdSlotItem(DemoConfig.FEED_UNIFIED_ID)
            )
        )
        rewardAdapter.submitList(listOf(AdSlotItem(DemoConfig.REWARD_ID)))
    }
}