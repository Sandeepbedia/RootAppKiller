package com.io.root.killer.data

import com.io.root.killer.root.RootManager

/**
 * The only place in the app that actually issues `am force-stop`. Every
 * call re-checks [AppInfo.isProtected] and [AppInfo.runState] itself —
 * callers are never trusted to have already filtered correctly, since
 * this is the last line of defense against stopping something critical.
 */
object ForceStopEngine {

    suspend fun forceStop(app: AppInfo): StopResult {
        if (app.isProtected) {
            return StopResult(app.packageName, app.appName, false, "Protected — skipped")
        }
        if (app.runState == RunState.FOREGROUND) {
            return StopResult(app.packageName, app.appName, false, "Running in foreground — skipped")
        }
        if (app.runState == RunState.STOPPED) {
            return StopResult(app.packageName, app.appName, true, "Already stopped")
        }

        val result = RootManager.run("am force-stop ${app.packageName}")
        return if (result.success) {
            StopResult(app.packageName, app.appName, true, "Stopped")
        } else {
            val reason = result.output.firstOrNull()?.takeIf { it.isNotBlank() } ?: "Command failed"
            StopResult(app.packageName, app.appName, false, reason)
        }
    }

    suspend fun forceStopMany(apps: List<AppInfo>): List<StopResult> =
        apps.map { forceStop(it) }
}
