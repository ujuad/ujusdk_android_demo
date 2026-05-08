package com.ujuad.demo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ujuad.demo.MyApplication
import com.ujuad.demo.R
import com.ujuad.demo.constant.DemoConfig
import com.ujuad.demo.databinding.FragmentHomeBinding
import com.ujuad.demo.viewmodel.AppViewModel
import kotlinx.coroutines.launch

/**
 * 首页Fragment，展示应用概览和快速操作入口
 */
class HomeFragment : Fragment() {

    private val appViewModel: AppViewModel by lazy {
        (requireActivity().application as MyApplication).appViewModel
    }
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        createObserver()
    }

    private fun createObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            // 只有在生命周期至少处于 STARTED 状态时才收集
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 使用 AppViewModel 监听SDK初始化状态
                appViewModel.isSdkInitialized.collect { isInitialized ->
                    updateSdkStatus(isInitialized)
                }
            }
        }
    }


    /**
     * 设置所有按钮的点击监听器
     */
    private fun initView() {
        binding.tvAppId.text = DemoConfig.APP_ID

        // 权限管理按钮
        binding.btnPermission.setOnClickListener {
            //跳转到权限管理页面
            navigateToFragment(PermissionFragment())
        }

        // 关于按钮
        binding.btnAbout.setOnClickListener {
            //跳转到关于页面
            navigateToFragment(AboutFragment())
        }

        binding.btnAdList.setOnClickListener {
            //跳转到广告列表页面
            navigateToFragment(AdGroupListFragment())
        }

        binding.btnAdMix.setOnClickListener {
            //跳转到聚合广告页面
            navigateToFragment(AdMixFragment())
        }

        binding.btnVersion.setOnClickListener {
            //跳转到版本信息页面
            navigateToFragment(VersionFragment())
        }
    }

    /**
     * 导航到指定Fragment
     * @param fragment 要跳转的Fragment实例
     */
    private fun navigateToFragment(fragment: Fragment) {
        try {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.flContainer, fragment)
                .addToBackStack(null) // 添加到返回栈，允许用户返回
                .commit()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "页面跳转失败: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * 更新SDK状态显示
     * @param Boolean
     */
    private fun updateSdkStatus(isInitialized: Boolean) {
        if (isInitialized) {
            binding.initStatus.text = "已初始化"
            binding.initStatus.setTextColor(resources.getColor(R.color.success, null))
        } else {
            binding.initStatus.text = "未初始化"
            binding.initStatus.setTextColor(resources.getColor(R.color.error, null))
        }
    }
}
