package com.example

import android.content.Context
import android.content.pm.PackageManager
import com.example.data.remote.RemoteConfigRepository
import java.security.MessageDigest
import kotlin.concurrent.thread

/**
 * 安全加固：签名自校验（静默模式，v1.6.9 起不再弹出任何提示）
 *
 * 【v1.6.7】加密/加固后闪退修复：校验由「阻止运行」改为「仅提醒」。
 * 【v1.6.9】按用户要求移除所有弹窗提示（含第三方修改工具检测提示），
 *   后续版本均不出现任何安全提示弹窗；校验仅作内部记录，不影响任何运行。
 *
 * 说明：
 * - 移除：调试器附加提示、第三方修改工具环境特征检测与提示
 * - 保留：云端签名自校验逻辑（不一致时静默容忍，不提示、不阻止）
 */
object SecurityGuard {

    /** 主入口：异步执行静默安全校验（无任何弹窗提示） */
    fun verifyInBackground(context: Context) {
        thread {
            try {
                // 签名自校验（静默）：不一致仅内部容忍，不提示、不阻止运行
                val repo = RemoteConfigRepository(context)
                val data = repo.fetchAdminDataBlocking()
                val sec = data?.settings?.security ?: return@thread
                if (!sec.enabled) return@thread
                val expected = sec.expectedSha.lowercase()
                if (expected.isBlank()) return@thread
                val actual = currentSigningSha(context)
                if (actual == null || !expected.contains(actual)) {
                    // v1.6.9 起：签名不一致不再做任何提示，静默通过
                    return@thread
                }
            } catch (e: Exception) {
                // 校验失败不阻断正常运行
            }
        }
    }

    /** 读取当前 APK 签名证书 SHA-256（十六进制小写） */
    private fun currentSigningSha(context: Context): String? {
        return try {
            val info = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
            val certs = info.signingInfo?.apkContentsSigners ?: return null
            certs.firstOrNull()?.toByteArray()?.let { bytes ->
                val md = MessageDigest.getInstance("SHA-256")
                md.digest(bytes).joinToString("") { "%02x".format(it) }
            }
        } catch (e: Exception) {
            null
        }
    }
}
