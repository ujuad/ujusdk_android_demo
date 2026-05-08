package com.ujuad.demo.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * 权限管理ViewModel，用于管理和观察应用权限状态
 */
class PermissionViewModel(application: Application) : AndroidViewModel(application) {

    // 权限状态LiveData
    private val _permissionsState = MutableLiveData<List<PermissionState>>()
    val permissionsState: LiveData<List<PermissionState>> = _permissionsState

    // 应用权限整体状态
    private val _appPermissionStatus = MutableLiveData<AppPermissionStatus>()
    val appPermissionStatus: LiveData<AppPermissionStatus> = _appPermissionStatus

    // 需要检查的权限列表
    private val requiredPermissions: List<PermissionInfo> by lazy {
        val permissions = mutableListOf(
            // 基础网络权限
            PermissionInfo(Manifest.permission.INTERNET, "网络权限", "用于加载广告内容"),
            PermissionInfo(Manifest.permission.ACCESS_NETWORK_STATE, "网络状态权限", "用于检查网络连接"),
            PermissionInfo(Manifest.permission.ACCESS_WIFI_STATE, "WiFi状态权限", "用于优化广告加载"),

            // 位置权限
            PermissionInfo(Manifest.permission.ACCESS_COARSE_LOCATION, "位置权限", "用于提供基于位置的广告"),
            PermissionInfo(Manifest.permission.ACCESS_FINE_LOCATION, "精确位置权限", "用于提供更精准的基于位置的广告"),

            // 设备信息权限
            PermissionInfo(Manifest.permission.READ_PHONE_STATE, "电话状态权限", "用于广告追踪和分析"),

            // 系统权限
            PermissionInfo(Manifest.permission.RECEIVE_BOOT_COMPLETED, "开机自启动权限", "用于广告服务的初始化"),

            // 广告相关权限
            PermissionInfo(Manifest.permission.QUERY_ALL_PACKAGES, "应用查询权限", "用于广告投放和分析")
        )

        // 根据Android版本添加不同的存储权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13及以上版本使用新的存储权限
            permissions.add(PermissionInfo(Manifest.permission.READ_MEDIA_IMAGES, "读取图片权限", "用于缓存广告图片资源"))
            permissions.add(PermissionInfo(Manifest.permission.READ_MEDIA_VIDEO, "读取视频权限", "用于缓存广告视频资源"))
            permissions.add(PermissionInfo(Manifest.permission.READ_MEDIA_AUDIO, "读取音频权限", "用于缓存广告音频资源"))
        } else {
            // Android 12及以下版本使用旧的存储权限
            permissions.add(PermissionInfo(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储权限", "用于缓存广告资源"))
            permissions.add(PermissionInfo(Manifest.permission.READ_EXTERNAL_STORAGE, "读取存储权限", "用于读取广告缓存资源"))
        }

        // Android 13及以上版本添加广告ID权限
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(PermissionInfo(Manifest.permission.AD_ID, "广告ID权限", "用于广告个性化和追踪"))
        }*/

        permissions
    }

    /**
     * 初始化时检查所有权限状态
     */
    init {
        checkAllPermissions()
    }

    /**
     * 检查所有权限状态并更新LiveData
     */
    fun checkAllPermissions() {
        val context = getApplication<Application>().applicationContext
        val states = requiredPermissions.map {
            PermissionState(
                permission = it.permission,
                name = it.name,
                description = it.description,
                isGranted = isPermissionGranted(context, it.permission)
            )
        }

        _permissionsState.value = states
        updateAppPermissionStatus(states)
    }

    /**
     * 更新应用整体权限状态
     */
    private fun updateAppPermissionStatus(permissionStates: List<PermissionState>) {
        val allGranted = permissionStates.all { it.isGranted }
        val partiallyGranted = permissionStates.any { it.isGranted }

        _appPermissionStatus.value = when {
            allGranted -> AppPermissionStatus.ALL_GRANTED
            partiallyGranted -> AppPermissionStatus.PARTIALLY_GRANTED
            else -> AppPermissionStatus.NONE_GRANTED
        }
    }

    /**
     * 检查单个权限是否已授予
     */
    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        return when (permission) {
            // 特殊权限处理
            Manifest.permission.SYSTEM_ALERT_WINDOW -> {
                // 检查是否允许在其他应用上层显示
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Settings.canDrawOverlays(context)
                } else {
                    true // 低于Android 6.0版本默认允许
                }
            }
            Manifest.permission.REQUEST_INSTALL_PACKAGES -> {
                // 检查是否允许安装应用
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.packageManager.canRequestPackageInstalls()
                } else {
                    true // 低于Android 8.0版本默认允许
                }
            }
            // 普通权限处理
            else -> {
                ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    /**
     * 获取需要请求的普通权限列表
     * 特殊权限（如SYSTEM_ALERT_WINDOW、REQUEST_INSTALL_PACKAGES）需要单独处理
     * 网络权限（INTERNET, ACCESS_NETWORK_STATE, ACCESS_WIFI_STATE）是普通权限，不需要动态请求
     */
    fun getPermissionsToRequest(): Array<String> {
        val context = getApplication<Application>().applicationContext
        return requiredPermissions
            .filter {
                !isPermissionGranted(context, it.permission) &&
                // 排除特殊权限，它们需要单独处理
                it.permission != Manifest.permission.SYSTEM_ALERT_WINDOW &&
                it.permission != Manifest.permission.REQUEST_INSTALL_PACKAGES &&
                // 排除网络权限，它们是普通权限，不需要动态请求
                it.permission != Manifest.permission.INTERNET &&
                it.permission != Manifest.permission.ACCESS_NETWORK_STATE &&
                it.permission != Manifest.permission.ACCESS_WIFI_STATE
            }
            .map { it.permission }
            .toTypedArray()
    }

    /**
     * 权限信息数据类
     */
    data class PermissionInfo(val permission: String, val name: String, val description: String)

    /**
     * 权限状态数据类
     */
    data class PermissionState(
        val permission: String,
        val name: String,
        val description: String,
        val isGranted: Boolean
    )

    /**
     * 应用权限整体状态枚举
     */
    enum class AppPermissionStatus {
        ALL_GRANTED,     // 所有权限已授予
        PARTIALLY_GRANTED, // 部分权限已授予
        NONE_GRANTED     // 没有权限授予
    }

    /**
     * 隐私偏好设置数据类
     */
    data class PrivacyPreferences(
        var isGdprConsentGranted: Boolean = false, // GDPR同意状态
        var isCcpaOptOut: Boolean = false, // CCPA选择退出状态
        var isAdPersonalizationEnabled: Boolean = true // 广告个性化启用状态
    )

    // 隐私偏好设置LiveData
    private val _privacyPreferences = MutableLiveData<PrivacyPreferences>()
    val privacyPreferences: LiveData<PrivacyPreferences> = _privacyPreferences

    /**
     * 更新GDPR同意状态
     */
    fun updateGdprConsent(granted: Boolean) {
        val current = _privacyPreferences.value ?: PrivacyPreferences()
        _privacyPreferences.value = current.copy(isGdprConsentGranted = granted)
    }

    /**
     * 更新CCPA选择退出状态
     */
    fun updateCcpaOptOut(optOut: Boolean) {
        val current = _privacyPreferences.value ?: PrivacyPreferences()
        _privacyPreferences.value = current.copy(isCcpaOptOut = optOut)
    }

    /**
     * 更新广告个性化启用状态
     */
    fun updateAdPersonalizationEnabled(enabled: Boolean) {
        val current = _privacyPreferences.value ?: PrivacyPreferences()
        _privacyPreferences.value = current.copy(isAdPersonalizationEnabled = enabled)
    }

    /**
     * 检查是否需要显示隐私授权
     */
    fun shouldShowPrivacyConsent(): Boolean {
        val current = _privacyPreferences.value ?: PrivacyPreferences()
        // 如果GDPR同意未授予，则需要显示隐私授权
        return !current.isGdprConsentGranted
    }
}