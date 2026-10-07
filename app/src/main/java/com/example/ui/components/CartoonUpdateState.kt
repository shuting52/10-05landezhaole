package com.example.ui.components

/**
 * 卡通更新弹窗状态机（v1.7.6 参照 AppUpdater 移植）。
 * AppUpdateDialog 将现有云端更新流程映射为该状态，驱动卡通弹窗渲染。
 */
sealed class CartoonUpdateState {
    /** 初始空闲态 */
    object Idle : CartoonUpdateState()

    /** 正在检测新版本 */
    object Checking : CartoonUpdateState()

    /** 检测到新版本，等待用户确认更新 */
    data class Found(val info: CartoonUpdateInfo) : CartoonUpdateState()

    /** 正在下载 APK */
    data class Downloading(
        val progress: Float,
        val bytesDownloaded: Long,
        val totalBytes: Long
    ) : CartoonUpdateState()

    /** APK 已下载完毕，等待安装 */
    object DownloadReady : CartoonUpdateState()

    /** 正在安装（系统安装会话提交中） */
    object Installing : CartoonUpdateState()

    /** 需要用户先开启「允许安装未知应用」权限 */
    object NeedInstallPermission : CartoonUpdateState()

    /** 更新成功 */
    data class Done(val installed: Boolean) : CartoonUpdateState()

    /** 出错（下载失败 / 安装失败 / 已是最新） */
    data class Error(val message: String, val canRetry: Boolean = true) : CartoonUpdateState()
}

/** 卡通弹窗展示用的更新信息（含写死的叮咚文案） */
data class CartoonUpdateInfo(
    val versionCode: Int = 0,
    val versionName: String = "",
    val downloadUrl: String = "",
    val apkSize: Long = 0L,
    val releaseNotes: List<String> = CartoonUpdateInfo.FIXED_NOTES,
    val forceUpdate: Boolean = false,
    val isDemo: Boolean = false
) {
    companion object {
        /** v1.7.4 起写死的固定更新文案（后续发布任何版本都不变） */
        val FIXED_NOTES = listOf(
            "叮咚～我们又又又更新啦！",
            "赶紧快来看看新版本有什么好宝贝吧",
            "我们一直在白嫖的路上，一直在奔跑哟",
            "快点更新吧～期待您发现自己的新大陆。"
        )
    }
}
