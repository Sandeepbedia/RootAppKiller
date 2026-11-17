package com.io.root.killer.data

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.io.root.killer.root.RootManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {

    private val packageManager = context.packageManager

    /**
     * Builds the full app list with live background/foreground state.
     * Requires an already-granted root shell (for the process/foreground
     * probes); the installed-app enumeration itself works without root.
     */
    suspend fun loadApps(): List<AppInfo> = withContext(Dispatchers.Default) {
        val protectedSet = ProtectedPackages.build(context)
        val runningPackages = runningProcessPackages()
        val foregroundPackage = foregroundPackage()

        @Suppress("DEPRECATION")
        val installed = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        installed
            .filter { it.packageName != null }
            .map { appInfo -> toAppInfo(appInfo, protectedSet, runningPackages, foregroundPackage) }
            .sortedWith(compareBy({ it.runState != RunState.BACKGROUND }, { it.appName.lowercase() }))
    }

    private fun toAppInfo(
        appInfo: ApplicationInfo,
        protectedSet: Set<String>,
        runningPackages: Set<String>,
        foregroundPackage: String?
    ): AppInfo {
        val pkg = appInfo.packageName
        val isSystem = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        val label = try {
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            pkg
        }
        val icon = try {
            packageManager.getApplicationIcon(appInfo)
        } catch (e: Exception) {
            null
        }

        val runState = when {
            pkg == foregroundPackage -> RunState.FOREGROUND
            runningPackages.contains(pkg) -> RunState.BACKGROUND
            else -> RunState.STOPPED
        }

        return AppInfo(
            packageName = pkg,
            appName = label,
            icon = icon,
            isSystemApp = isSystem,
            isProtected = protectedSet.contains(pkg),
            runState = runState
        )
    }

    /**
     * Package names with at least one live process, read from `ps`.
     * Process names are usually the package name itself, optionally with
     * a `:suffix` for a secondary process (e.g. `com.app:service`), which
     * is stripped so both roll up under the same package.
     */
    private suspend fun runningProcessPackages(): Set<String> {
        val result = RootManager.run("ps -A -o NAME 2>/dev/null || ps -A")
        if (!result.success) return emptySet()

        return result.output
            .mapNotNull { line ->
                val token = line.trim().substringAfterLast(' ')
                if (token.contains('.') && token != "NAME") token.substringBefore(':') else null
            }
            .toSet()
    }

    /** Best-effort foreground package via WindowManager's current focus window. */
    private suspend fun foregroundPackage(): String? {
        val result = RootManager.run(
            "dumpsys window 2>/dev/null | grep -m 1 mCurrentFocus"
        )
        if (!result.success) return null
        val line = result.output.firstOrNull() ?: return null
        // Example: mCurrentFocus=Window{... u0 com.example.app/com.example.app.MainActivity}
        val afterSpace = line.substringAfterLast(' ').removeSuffix("}")
        return afterSpace.substringBefore('/').takeIf { it.contains('.') }
    }
}
