package com.ujuad.demo.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ujuad.demo.R

/**
 * @CreateDate: 2025/12/19 16:52
 * @Author: 青柠
 * @Description: 自定义标题栏控件，包含返回按钮、标题和右侧图标
 */
class TitleBar : FrameLayout {

    private lateinit var backButton: ImageView
    private lateinit var titleText: TextView
    private lateinit var rightIcon: ImageView

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.view_title_bar, this, true)

        backButton = findViewById(R.id.iv_back)
        titleText = findViewById(R.id.tv_title)

        // 设置返回按钮点击事件
        backButton.setOnClickListener {
            (context as AppCompatActivity).supportFragmentManager.popBackStack()
        }
    }

    /**
     * 设置标题文本
     * @param title 标题内容
     */
    fun setTitle(title: String) {
        titleText.text = title
    }

    /**
     * 设置右侧图标资源
     * @param resId 图标资源ID
     * @param clickListener 点击事件监听器
     */
    fun setRightIcon(resId: Int, clickListener: (() -> Unit)? = null) {
        rightIcon.setImageResource(resId)
        rightIcon.visibility = VISIBLE
        clickListener?.let {
            rightIcon.setOnClickListener { it() }
        }
    }

    /**
     * 隐藏右侧图标
     */
    fun hideRightIcon() {
        rightIcon.visibility = GONE
    }

    /**
     * 隐藏返回按钮
     */
    fun hideBackButton() {
        backButton.visibility = GONE
    }
}
