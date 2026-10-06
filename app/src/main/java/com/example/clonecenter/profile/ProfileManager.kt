package com.example.clonecenter.profile

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.UserHandle
import android.os.UserManager
import android.provider.Settings
import com.example.clonecenter.admin.CloneAdminReceiver

/**
 * Work Profile（工作分身空间）管理器
 * 通过系统 DevicePolicyManager 创建隔离的托管资料，实现真正意义的应用分身
 */
class ProfileManager(
    private val context: Context
) {
    private val dpm =
        context.getSystemService(
            Context.DEVICE_POLICY_SERVICE
        ) as DevicePolicyManager

    private val admin = ComponentName(
        context,
        CloneAdminReceiver::class.java
    )

    /** 设备是否支持托管用户（Work Profile 前提条件） */
    fun supportsManagedProfile(): Boolean {
        return context.packageManager.hasSystemFeature(
            PackageManager.FEATURE_MANAGED_USERS
        )
    }

    /** 发起 Work Profile 创建流程（系统引导界面） */
    fun startProvisioning(activity: Activity): Boolean {
        if (!supportsManagedProfile()) {
            return false
        }

        val intent = Intent(
            DevicePolicyManager.ACTION_PROVISION_MANAGED_PROFILE
        ).apply {
            putExtra(
                DevicePolicyManager
                    .EXTRA_PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME,
                admin
            )

            putExtra(
                DevicePolicyManager
                    .EXTRA_PROVISIONING_SKIP_ENCRYPTION,
                false
            )

            putExtra(
                DevicePolicyManager
                    .EXTRA_PROVISIONING_LEAVE_ALL_SYSTEM_APPS_ENABLED,
                true
            )
        }

        if (intent.resolveActivity(
                context.packageManager
            ) == null
        ) {
            return false
        }

        activity.startActivityForResult(
            intent,
            REQUEST_PROVISION_PROFILE
        )

        return true
    }

    /** 当前应用是否为 Profile Owner */
    fun isProfileOwner(): Boolean {
        return dpm.isProfileOwnerApp(
            context.packageName
        )
    }

    /** 启用 Profile（Profile Owner 回调中调用） */
    fun enableProfile() {
        if (!isProfileOwner()) {
            return
        }

        dpm.setProfileName(
            admin,
            "分身空间"
        )

        dpm.setProfileEnabled(admin)
    }

    /** 配置分身空间限制（禁止从主空间分享内容进分身空间） */
    fun setProfileRestrictions() {
        if (!isProfileOwner()) {
            return
        }

        dpm.addUserRestriction(
            admin,
            UserManager.DISALLOW_SHARE_INTO_MANAGED_PROFILE
        )
    }

    /**
     * 获取工作分身空间（Work Profile）用户的 UserHandle，不存在返回 null。
     * 注：精简版 SDK stub 移除了部分隐藏 API，这里用「非主用户（id != 0）」判定托管空间，
     * 对普通消费级设备（主用户 id 恒为 0）成立。
     */
    fun getManagedProfileHandle(): UserHandle? {
        return try {
            val um = context.getSystemService(
                Context.USER_SERVICE
            ) as UserManager
            val me = android.os.Process.myUserHandle()
            um.userProfiles.firstOrNull { handle ->
                // 主用户（当前进程所在用户）排除；其余非 0 用户即工作分身空间（精简 stub 无 id/getIdentifier，用 equals）
                !handle.equals(me)
            }
        } catch (e: Exception) {
            null
        }
    }

    /** 工作分身空间（Work Profile）是否已启用（桌面上存在「工作」标签） */
    fun isManagedProfileActive(): Boolean {
        return getManagedProfileHandle() != null
    }

    /**
     * 移除工作分身空间（删除桌面「工作」标签及其全部分身应用）
     * 需本应用为 Profile Owner；移除成功后返回 true
     */
    fun removeManagedProfile(): Boolean {
        return try {
            val handle = getManagedProfileHandle() ?: return true
            if (!isProfileOwner()) return false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dpm.removeUser(admin, handle)
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    /** 打开系统「账号与同步」设置页（可在此管理分身空间的账号/资料） */
    fun openWorkProfileSettings(): Boolean {
        return try {
            val intent = Intent(Settings.ACTION_SYNC_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }

    /** 目标应用是否已存在于工作分身空间（createPackageContextAsUser 为隐藏 API，改用反射调用） */
    fun isPackageInManagedProfile(packageName: String): Boolean {
        val handle = getManagedProfileHandle() ?: return false
        return try {
            val method = Context::class.java.getMethod(
                "createPackageContextAsUser",
                String::class.java,
                Int::class.javaPrimitiveType,
                UserHandle::class.java
            )
            method.invoke(context, packageName, 0, handle)
            true
        } catch (e: Exception) {
            false
        }
    }

    /** 将已安装应用安装进分身空间（需 Profile Owner） */
    fun installExistingPackage(
        packageName: String
    ): Boolean {
        // 已在分身空间：视为成功（避免重复安装报错）
        if (isPackageInManagedProfile(packageName)) {
            return true
        }
        if (!isProfileOwner()) {
            return false
        }

        return try {
            dpm.installExistingPackage(
                admin,
                packageName
            )
        } catch (e: SecurityException) {
            false
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        const val REQUEST_PROVISION_PROFILE = 2001
    }
}
