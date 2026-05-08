package com.ujuad.demo.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ujuad.demo.R
import com.ujuad.demo.databinding.FragmentVersionBinding
import com.ujuad.demo.model.AdnAdapterVersionModel
import com.ujuad.demo.ui.adapter.AdnAdapterVersionAdapter
import com.ujusdk.adcore.platforms.AdAdapterRegistry
import com.ujusdk.adcore.public.UjuAdSdk
import com.ujusdk.adcore.public.enums.AdPlatformType

/**
 * @CreateDate: 2026/3/10 18:27
 * @Author: 青柠
 * @Description: 
 */
class VersionFragment : Fragment() {
    private lateinit var binding: FragmentVersionBinding

    private lateinit var adnVersionAdapter: AdnAdapterVersionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVersionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAdapters()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.titleBar.setTitle("版本信息")
        binding.tvSdkVersion.text = "Android ${UjuAdSdk.getVersion()}"
        binding.tvAppName.text = UjuAdSdk.getAppName()

        binding.tvOaid.text = "OAID: ${UjuAdSdk.getOAID()}"
        binding.tvGoogleId.text = "Google ID: ${UjuAdSdk.getGoogleAdId()}"
    }

    private fun initAdapters() {
        adnVersionAdapter = AdnAdapterVersionAdapter().apply {
            setOnItemClickListener { item ->
            }
        }

        binding.rvAdn.apply {
            adapter = adnVersionAdapter
            layoutManager = LinearLayoutManager(context)
        }

        val adapterList = AdAdapterRegistry.getAdnAdapterFactories()
        val modelList: MutableList<AdnAdapterVersionModel> = mutableListOf()
        for ((_, value) in adapterList) {
            val model = AdnAdapterVersionModel(
                adnName = AdPlatformType.fromValue(value.getPlatformId()).displayName,
                adnVersion = value.getVersion(),
                adapterVersion = value.getAdapterVersion(),
                adapterLogoResource = getAdnLogoResource(value.getPlatformId())
            )
            modelList.add(model)
        }
        adnVersionAdapter.submitList(modelList)
    }

    private fun getAdnLogoResource(newWorkId: Int): Int {
        return when (newWorkId) {
            AdPlatformType.CSJ.value -> {
                R.drawable.ad_logo_csj
            }

            AdPlatformType.YLH.value -> {
                R.drawable.ad_logo_ylh
            }

            AdPlatformType.BD.value -> {
                R.drawable.ad_logo_bd
            }

            AdPlatformType.KS.value -> {
                R.drawable.ad_logo_ks
            }

            else -> {
                R.drawable.ic_logo
            }
        }

    }
}