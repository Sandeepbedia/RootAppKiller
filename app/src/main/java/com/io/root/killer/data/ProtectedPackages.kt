package com.io.root.killer.data

import android.content.Context
import android.content.Intent
import com.io.root.killer.root.RootManagerType

/**
 * Packages this app will refuse to force-stop no matter what the user
 * selects. Covers the device's current launcher (resolved at runtime,
 * since that varies by device/ROM), every known root-manager package,
 * this app itself, and a fixed list of AOSP system processes whose loss
 * would destabilize the device rather than just closing an app.
 */
object ProtectedPackages {

    private val FIXED_CRITICAL = setOf(
        "android",                              // the system framework package
        "com.android.systemui",                 // System UI / status & nav bars
        "com.android.settings",                 // Settings app
        "com.android.phone",                     // Telephony service
        "com.android.providers.telephony",
        "com.android.providers.settings",
        "com.android.providers.media",
        "com.android.providers.media.module",
        "com.android.bluetooth",
        "com.android.nfc",
        "com.android.server.telecom",
        "com.android.systemui.theme",
        "com.google.android.gms",               // Google Play services
        "com.google.android.gsf",
        "com.android.vending",                  // Play Store (avoid breaking updates mid-use)
        "com.android.inputmethod.latin"          // default AOSP keyboard
    )

    private val ROOT_MANAGER_PACKAGES = setOf(
        RootManagerType.MAGISK.managerPackage,
        RootManagerType.KERNEL_SU.managerPackage,
        RootManagerType.APATCH.managerPackage,
        "io.github.huskydg.magisk",   // common Magisk delta/alt builds
        "me.weishu.kernelsu.next"      // KernelSU-Next fork
    ).filterNotNull().toSet()

    fun build(context: Context): Set<String> {
        val launcherPackage = resolveLauncherPackage(context)
        val selfPackage = context.packageName
        return FIXED_CRITICAL + ROOT_MANAGER_PACKAGES + setOfNotNull(launcherPackage, selfPackage)
    }

    private fun resolveLauncherPackage(context: Context): String? {
        val homeIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
        val resolved = context.packageManager.resolveActivity(homeIntent, 0)
        return resolved?.activityInfo?.packageName
    }
}
