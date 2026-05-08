package com.ujuad.demo.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.ujuad.demo.R
import com.ujuad.demo.databinding.ActivityMainBinding
import com.ujuad.demo.ui.fragment.HomeFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 应用的主Activity，负责显示欢迎页和首页内容
 * 使用Material3设计风格
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var splashJob: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 设置沉浸式状态栏
        WindowCompat.setDecorFitsSystemWindows(window, false)

        showWelcomePage()
    }

    /**
     * 显示欢迎页，2秒后切换到首页
     */
    private fun showWelcomePage() {
        // 延迟后切换到首页
        splashJob = lifecycleScope.launch {
            delay(1000)
            switchToHomePage()
        }
    }

    /**
     * 切换到首页，加载HomeFragment
     */
    private fun switchToHomePage() {
        // 隐藏欢迎页
        binding.clSplash.visibility = View.GONE

        // 显示首页容器
        binding.flContainer.visibility = View.VISIBLE

        // 加载HomeFragment到首页容器
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.flContainer, homeFragment)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()

        splashJob?.cancel()
        splashJob = null
    }
}