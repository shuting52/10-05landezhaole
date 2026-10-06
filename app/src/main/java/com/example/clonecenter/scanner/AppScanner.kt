package com.example.clonecenter.scanner

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.example.clonecenter.model.InstalledApp

/**
 * 扫描手机上所有可启动（带 Launcher 入口）的应用
 * 需要 QUERY_ALL_PACKAGES 权限（已在 Manifest 声明）
 */
class AppScanner(
    private val context: Context
) {
    private val packageManager: PackageManager =
        context.packageManager

    fun scan(): List<InstalledApp> {
        val launcherIntent = Intent(
            Intent.ACTION_MAIN
        ).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        return packageManager
            .queryIntentActivities(
                launcherIntent,
                PackageManager.MATCH_ALL
            )
            .mapNotNull { result ->
                val activityInfo =
                    result.activityInfo ?: return@mapNotNull null

                val appInfo =
                    activityInfo.applicationInfo

                InstalledApp(
                    packageName = appInfo.packageName,
                    label = appInfo
                        .loadLabel(packageManager)
                        .toString(),
                    activityName = activityInfo.name,
                    isSystemApp =
                        appInfo.flags and
                            android.content.pm.ApplicationInfo.FLAG_SYSTEM
                            != 0,
                    icon = appInfo.loadIcon(packageManager)
                )
            }
            .distinctBy { it.packageName }
            .sortedBy { it.label.lowercase() }
    }
}
