package com.example.clonecenter.admin

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.clonecenter.profile.ProfileManager

/**
 * 设备管理接收器：Work Profile 创建完成回调
 * Profile 创建完成后系统回调 onProfileProvisioningComplete，
 * 在此启用 Profile 并配置限制策略。
 */
class CloneAdminReceiver : DeviceAdminReceiver() {

    override fun onProfileProvisioningComplete(
        context: Context,
        intent: Intent
    ) {
        val manager = ProfileManager(context)

        manager.enableProfile()
        manager.setProfileRestrictions()

        Toast.makeText(
            context,
            "分身空间创建完成",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onEnabled(
        context: Context,
        intent: Intent
    ) {
        Toast.makeText(
            context,
            "设备管理权限已启用",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDisabled(
        context: Context,
        intent: Intent
    ) {
        Toast.makeText(
            context,
            "设备管理权限已关闭",
            Toast.LENGTH_SHORT
        ).show()
    }
}
