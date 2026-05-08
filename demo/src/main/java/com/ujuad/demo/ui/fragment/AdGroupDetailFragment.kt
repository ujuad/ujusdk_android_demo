package com.ujuad.demo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ujuad.demo.databinding.FragmentAdGroupDetailBinding
import com.ujuad.demo.ext.bundleOf
import com.ujuad.demo.ext.navigateFragment
import com.ujuad.demo.ui.adapter.AdGroupDetailAdapter
import com.ujusdk.adcore.strategy.AdStrategyManager
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * @CreateDate: 2025/12/16 17:46
 * @Author: 青柠
 * @Description: 广告位详情列表，即代码位
 */
class AdGroupDetailFragment : Fragment() {

    private var _binding: FragmentAdGroupDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var detailAdapter: AdGroupDetailAdapter

    //广告类型
    private var title = ""

    //广告位ID
    private var placementId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdGroupDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.run {
            title = getString("title") ?: ""
            placementId = getString("placementId") ?: ""
        }

        initView()
        setupRecyclerViews()
        getAdGroupList()
    }

    private fun initView() {
        binding.titleBar.setTitle("广告位详情")
        binding.tvTitle.text = "广告类型：${title}"
        binding.tvAdCode.text = "广告位ID：${placementId}"
        initAdapters()
    }

    private fun initAdapters() {
        detailAdapter = AdGroupDetailAdapter().apply {
            setOnItemClickListener { item ->

            }
        }
    }

    private fun setupRecyclerViews() {
        binding.rvAdGroupDetail.apply {
            adapter = detailAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getAdGroupList() {
        lifecycleScope.launch {
            //从已加载的广告位信息获取
            val list = AdStrategyManager.getUnitSettings(placementId)

            //判断是否已加载，如果未加载，则从服务器获取
            if (list.isNullOrEmpty()) {
                val isLoaded = async {
                    AdStrategyManager.isPlacementLoaded(placementId)
                }
                if (isLoaded.await()) {
                    detailAdapter.submitList(AdStrategyManager.getUnitSettings(placementId))
                }
            } else {
                detailAdapter.submitList(list)
            }
        }

    }
}