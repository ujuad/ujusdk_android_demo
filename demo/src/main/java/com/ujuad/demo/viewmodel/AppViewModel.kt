package com.ujuad.demo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @CreateDate: 2026/3/11 14:33
 * @Author: 青柠
 * @Description:
 */
class AppViewModel(application: Application) : AndroidViewModel(application) {
    // SDK初始化状态
    val isSdkInitialized = MutableStateFlow(false)
}