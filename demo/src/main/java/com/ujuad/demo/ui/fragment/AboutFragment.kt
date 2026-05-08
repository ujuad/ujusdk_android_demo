package com.ujuad.demo.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ujuad.demo.databinding.FragmentAboutBinding
import com.ujuad.demo.BuildConfig
import com.ujusdk.adcore.public.UjuAdSdk

/**
 * 关于页面Fragment，用于显示应用信息和SDK版本信息
 */
class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvAppVersion.text = "v${BuildConfig.VERSION_NAME}"
        binding.tvSdkVersion.text = UjuAdSdk.getVersion()

        // 设置网站和邮箱点击事件
        setupClickListeners()
    }

    /**
     * 设置点击监听器
     */
    private fun setupClickListeners() {
        // 网站点击事件
        binding.websiteInfo.setOnClickListener {
            // 这里可以实现打开浏览器访问官方网站的逻辑
            openWebsite("https://www.ujuad.com")
        }

        // 邮箱点击事件
        binding.emailInfo.setOnClickListener {
            // 这里可以实现打开邮件客户端发送邮件的逻辑
            openEmail("support@ujuad.com")
        }
    }

    /**
     * 打开网站
     * @param url 网站URL
     */
    private fun openWebsite(url: String) {
        // 实际项目中应该使用Intent打开浏览器
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            startActivity(this)
        }
    }

    /**
     * 打开邮件客户端
     * @param email 邮箱地址
     */
    private fun openEmail(email: String) {
        // 实际项目中应该使用Intent打开邮件客户端
        // 这里仅做演示，不实现具体逻辑
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
