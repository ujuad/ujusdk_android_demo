package com.ujuad.demo.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ujuad.demo.MyApplication
import com.ujuad.demo.R
import com.ujuad.demo.databinding.FragmentPermissionBinding
import com.ujuad.demo.viewmodel.PermissionViewModel

/**
 * 权限检查Fragment，用于查看和请求应用所需的权限
 */
class PermissionFragment : Fragment() {

    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!

    // ViewModel实例
    private lateinit var viewModel: PermissionViewModel

    // 运行时权限请求
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // 权限请求完成后，重新检查所有权限状态
        viewModel.checkAllPermissions()
        
        // 检查是否所有请求的权限都已授予
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            Toast.makeText(requireContext(), getString(R.string.all_permissions_granted), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), getString(R.string.some_permissions_denied), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初始化ViewModel
        viewModel = ViewModelProvider(this)[PermissionViewModel::class.java]

        // 设置标题
        binding.permissionTitle.text = getString(R.string.permission_title)
        binding.permissionsListTitle.text = getString(R.string.permissions_list_title)

        // 观察权限状态变化
        observePermissionChanges()

        // 设置按钮点击事件
        binding.requestAllPermissionsButton.setOnClickListener {
            requestAllPermissions()
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    /**
     * 观察权限状态变化并更新UI
     */
    private fun observePermissionChanges() {
        // 观察单个权限状态
        viewModel.permissionsState.observe(viewLifecycleOwner) { permissionStates ->
            updatePermissionStatus(permissionStates)
        }

        // 观察应用整体权限状态
        viewModel.appPermissionStatus.observe(viewLifecycleOwner) { status ->
            updateAppPermissionStatus(status)
        }
    }

    /**
     * 更新权限状态显示
     */
    private fun updatePermissionStatus(permissionStates: List<PermissionViewModel.PermissionState>) {
        // 清除现有内容
        binding.permissionListContainer.removeAllViews()

        // 检查context是否有效
        val context = context ?: return

        // 添加每个权限的状态视图
        for (permissionState in permissionStates) {
            val statusText = if (permissionState.isGranted) {
                getString(R.string.permission_granted)
            } else {
                getString(R.string.permission_denied)
            }
            val statusColor = if (permissionState.isGranted) {
                ContextCompat.getColor(context, R.color.success)
            } else {
                ContextCompat.getColor(context, R.color.error)
            }

            // 创建权限状态视图
            val permissionView = LayoutInflater.from(context)
                .inflate(R.layout.permission_item_layout, binding.permissionListContainer, false)

            // 设置权限信息
            permissionView.findViewById<TextView>(R.id.permission_name)?.text = permissionState.name
            permissionView.findViewById<TextView>(R.id.permission_description)?.text = permissionState.description
            val statusTextView = permissionView.findViewById<TextView>(R.id.permission_status)
            statusTextView?.text = statusText
            statusTextView?.setTextColor(statusColor)

            // 添加到容器
            binding.permissionListContainer.addView(permissionView)
        }
    }

    /**
     * 更新应用整体权限状态
     */
    private fun updateAppPermissionStatus(status: PermissionViewModel.AppPermissionStatus) {
        val statusText = when (status) {
            PermissionViewModel.AppPermissionStatus.ALL_GRANTED ->
                getString(R.string.all_permissions_granted)
            PermissionViewModel.AppPermissionStatus.PARTIALLY_GRANTED ->
                getString(R.string.some_permissions_denied)
            PermissionViewModel.AppPermissionStatus.NONE_GRANTED ->
                "所有必要权限未授予，广告功能可能受限"
        }
        
        binding.appPermissionStatus.text = statusText
    }

    /**
     * 请求所有权限
     */
    private fun requestAllPermissions() {
        val permissionsToRequest = viewModel.getPermissionsToRequest()

        if (permissionsToRequest.isNotEmpty()) {
            // 先检查是否需要显示隐私授权
            if (viewModel.shouldShowPrivacyConsent()) {
                showPrivacyConsentDialog(permissionsToRequest)
            } else {
                // 显示权限请求说明对话框
                showPermissionExplanationDialog(permissionsToRequest)
            }
        } else {
            Toast.makeText(requireContext(), getString(R.string.all_permissions_granted), Toast.LENGTH_SHORT).show()
            // 所有权限已授予，关闭当前页面
            requireActivity().finish()
        }
    }

    /**
     * 显示隐私授权对话框
     */
    private fun showPrivacyConsentDialog(permissionsToRequest: Array<String>) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.privacy_consent_title))
            .setMessage(getString(R.string.privacy_consent_message))
            .setPositiveButton(getString(R.string.agree)) {
                _, _ ->
                // 更新隐私偏好设置
                viewModel.updateGdprConsent(true)
                viewModel.updateCcpaOptOut(false)
                viewModel.updateAdPersonalizationEnabled(true)
                
                // 显示权限请求说明对话框
                showPermissionExplanationDialog(permissionsToRequest)
            }
            .setNegativeButton(getString(R.string.disagree)) {
                dialog, _ ->
                dialog.dismiss()
                Toast.makeText(requireContext(), getString(R.string.privacy_consent_canceled), Toast.LENGTH_SHORT).show()
                // 关闭当前页面，返回MainActivity
                requireActivity().finish()
            }
            .setNeutralButton(getString(R.string.settings)) {
                _, _ ->
                // 显示隐私设置对话框
                showPrivacySettingsDialog(permissionsToRequest)
            }
            .show()
    }

    /**
     * 显示隐私设置对话框
     */
    private fun showPrivacySettingsDialog(permissionsToRequest: Array<String>) {
        // 这里可以实现更详细的隐私设置UI
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.privacy_settings_title))
            .setMessage(getString(R.string.privacy_settings_message))
            .setPositiveButton(getString(R.string.save)) {
                _, _ ->
                // 保存设置后请求权限
                showPermissionExplanationDialog(permissionsToRequest)
            }
            .setNegativeButton(getString(R.string.cancel)) {
                dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    /**
     * 显示权限请求说明对话框
     */
    private fun showPermissionExplanationDialog(permissionsToRequest: Array<String>) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.permission_request_title))
            .setMessage(getString(R.string.permission_request_message))
            .setPositiveButton(getString(R.string.agree)) {
                _, _ ->
                // 同意后请求权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissionLauncher.launch(permissionsToRequest)
                }
            }
            .setNegativeButton(getString(R.string.disagree)) {
                dialog, _ ->
                dialog.dismiss()
                Toast.makeText(requireContext(), getString(R.string.permission_request_canceled), Toast.LENGTH_SHORT).show()
                // 关闭当前页面，返回MainActivity
                requireActivity().finish()
            }
            .setNeutralButton(getString(R.string.read_privacy_policy)) {
                _, _ ->
                // 这里可以添加跳转到隐私政策页面的逻辑
                Toast.makeText(requireContext(), "隐私政策页面待实现", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        // 每次回到此页面时，重新检查权限状态
        viewModel.checkAllPermissions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}