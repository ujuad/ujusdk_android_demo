package com.ujuad.demo.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import com.ujuad.demo.R

/**
 * @CreateDate: 2025/12/24 11:46
 * @Author: 青柠
 * @Description: 不可关闭的加载对话框
 */
class LoadingDialog(context: Context) : Dialog(context) {

    private var message: String = "加载中..."
    private var progressBar: ProgressBar? = null
    private var messageTextView: TextView? = null

    init {
        // 设置对话框样式
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//        //不允许外部关闭
//        setCancelable(false)
//        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)

        progressBar = findViewById(R.id.progressBar)
        messageTextView = findViewById(R.id.messageTextView)

        messageTextView?.text = message
    }

    fun setMessage(msg: String): LoadingDialog {
        message = msg
        messageTextView?.text = msg
        return this
    }

    override fun show() {
        if (!isShowing) {
            super.show()
        }
    }

    override fun dismiss() {
        if (isShowing) {
            super.dismiss()
        }
    }
}
