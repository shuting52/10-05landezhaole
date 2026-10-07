package com.example.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * v1.1.23：网络类型检测工具
 * 用于更新 APK 下载前识别当前网络是 WiFi 还是移动流量，
 * 移动流量大文件下载前给出提示，避免用户流量超额。
 */
object NetworkTypeDetector {

    sealed class NetType {
        object WIFI : NetType()
        object MOBILE : NetType()
        object OTHER : NetType()
    }

    /** 获取当前活跃网络类型（需 ACCESS_NETWORK_STATE 权限） */
    fun currentType(context: Context): NetType {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = cm.activeNetwork ?: return NetType.OTHER
            val caps = cm.getNetworkCapabilities(network) ?: return NetType.OTHER
            when {
                caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetType.WIFI
                caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetType.MOBILE
                caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetType.WIFI
                else -> NetType.OTHER
            }
        } catch (e: Exception) {
            NetType.OTHER
        }
    }

    /** 是否为移动流量网络 */
    fun isMobile(context: Context): Boolean = currentType(context) == NetType.MOBILE

    /** 是否为 WiFi（含有线） */
    fun isWifi(context: Context): Boolean = currentType(context) == NetType.WIFI
}
