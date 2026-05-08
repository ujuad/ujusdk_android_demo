package com.ujuad.demo.ext

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ujuad.demo.R

/**
 * @CreateDate: 2025/12/16 19:39
 * @Author: 青柠
 * @Description: NavController 扩展函数
 */
class NavController

// FragmentActivity 的扩展函数，用于跳转到 Fragment 并传递参数
fun FragmentActivity.navigateFragment(
    fragment: Fragment,
    args: Bundle? = null,
    addToBackStack: Boolean = true
) {
    args?.let {
        fragment.arguments = it
    }

    val transaction = supportFragmentManager.beginTransaction()
        .replace(R.id.flContainer, fragment)

    if (addToBackStack) {
        transaction.addToBackStack(null)
    }

    transaction.commit()
}

// Fragment 的扩展函数，用于跳转到另一个 Fragment 并传递参数
fun Fragment.navigateFragment(
    fragment: Fragment,
    args: Bundle? = null,
    addToBackStack: Boolean = true
) {
    args?.let {
        fragment.arguments = it
    }

    val transaction = requireActivity().supportFragmentManager.beginTransaction()
        .replace(R.id.flContainer, fragment)

    if (addToBackStack) {
        transaction.addToBackStack(null)
    }

    transaction.commit()
}

// Bundle 构建器扩展函数，方便创建参数
fun bundleOf(vararg pairs: Pair<String, Any?>): Bundle {
    return Bundle().apply {
        for ((key, value) in pairs) {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Double -> putDouble(key, value)
                is Boolean -> putBoolean(key, value)
                is Bundle -> putBundle(key, value)
                else -> putString(key, value?.toString())
            }
        }
    }
}

// Fragment 的扩展函数，用于关闭当前页面
fun Fragment.finishFragment() {
    requireActivity().supportFragmentManager.popBackStack()
}

// FragmentActivity 的扩展函数，用于关闭当前页面
fun FragmentActivity.finishFragment() {
    supportFragmentManager.popBackStack()
}

