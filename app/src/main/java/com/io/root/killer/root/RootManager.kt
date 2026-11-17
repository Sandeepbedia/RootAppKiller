package com.io.root.killer.root

import android.content.Context
import android.content.pm.PackageManager
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Thin wrapper around libsu's [Shell] for the one thing this app does with
 * root: run `am force-stop` / `pm` / `dumpsys` as root and read the result.
 *
 * Root prompts (Magisk / KernelSU / APatch's su dialog) are driven entirely
 * by the root manager app itself the first time a shell is opened — this
 * class never tries to script or auto-approve that dialog.
 */
object RootManager {

    init {
        Shell.enableVerboseLogging = false
        Shell.setDefaultBuilder(
            Shell.Builder.create()
                .setFlags(Shell.FLAG_REDIRECT_STDERR)
                .setTimeout(15)
        )
    }

    /**
     * Opens (or reuses) the root shell and reports which root manager is
     * present. Suspends on a background thread — opening a shell blocks
     * until the user approves or denies the prompt.
     */
    suspend fun requestRoot(context: Context): RootState = withContext(Dispatchers.IO) {
        val manager = detectManagerApp(context)
        val shell = try {
            Shell.getShell()
        } catch (e: Exception) {
            return@withContext RootState(
                granted = false,
                manager = manager,
                message = "Could not open a root shell: ${e.message ?: "unknown error"}"
            )
        }

        if (!shell.isRoot) {
            return@withContext RootState(
                granted = false,
                manager = manager,
                message = if (manager == RootManagerType.NONE) {
                    "No root manager found on this device."
                } else {
                    "${manager.label} is installed but root access was denied."
                }
            )
        }

        val resolvedManager = if (manager != RootManagerType.NONE) manager else detectManagerByShell()
        RootState(
            granted = true,
            manager = resolvedManager,
            message = "Root access granted via ${resolvedManager.label}."
        )
    }

    /** True if a previously opened shell is still root — does not prompt. */
    fun hasActiveRoot(): Boolean = Shell.isAppGrantedRoot() == true && Shell.getCachedShell()?.isRoot == true

    /** Runs one command as root and returns combined stdout (stderr is redirected into it). */
    suspend fun run(command: String): CommandResult = withContext(Dispatchers.IO) {
        val result = Shell.cmd(command).exec()
        CommandResult(result.isSuccess, result.code, result.out)
    }

    /** Runs several commands in one shell round-trip; faster than one call each. */
    suspend fun runAll(commands: List<String>): CommandResult = withContext(Dispatchers.IO) {
        if (commands.isEmpty()) return@withContext CommandResult(true, 0, emptyList())
        val result = Shell.cmd(*commands.toTypedArray()).exec()
        CommandResult(result.isSuccess, result.code, result.out)
    }

    /** Checks installed packages for a known root-manager app (no shell needed). */
    private fun detectManagerApp(context: Context): RootManagerType {
        val pm = context.packageManager
        fun installed(pkg: String) = try {
            pm.getPackageInfo(pkg, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return when {
            installed(RootManagerType.MAGISK.managerPackage!!) -> RootManagerType.MAGISK
            installed(RootManagerType.KERNEL_SU.managerPackage!!) -> RootManagerType.KERNEL_SU
            installed(RootManagerType.APATCH.managerPackage!!) -> RootManagerType.APATCH
            else -> RootManagerType.NONE
        }
    }

    /** Fallback when root was granted but no manager app was visible (e.g. hidden/relabeled). */
    private fun detectManagerByShell(): RootManagerType {
        val probe = Shell.cmd(
            "which magisk >/dev/null 2>&1 && echo magisk",
            "which ksud >/dev/null 2>&1 && echo kernelsu",
            "which apd >/dev/null 2>&1 && echo apatch"
        ).exec().out.joinToString(" ")
        return when {
            probe.contains("magisk") -> RootManagerType.MAGISK
            probe.contains("kernelsu") -> RootManagerType.KERNEL_SU
            probe.contains("apatch") -> RootManagerType.APATCH
            else -> RootManagerType.UNKNOWN_ROOT
        }
    }
}

/** Outcome of running one or more shell commands. */
data class CommandResult(
    val success: Boolean,
    val exitCode: Int,
    val output: List<String>
)
