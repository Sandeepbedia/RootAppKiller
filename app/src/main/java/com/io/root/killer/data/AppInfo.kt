package com.io.root.killer.data

import android.graphics.drawable.Drawable

enum class RunState { FOREGROUND, BACKGROUND, STOPPED }

data class AppInfo(
    val packageName: String,
    val appName: String,
    val icon: Drawable?,
    val isSystemApp: Boolean,
    val isProtected: Boolean,
    val runState: RunState
)

/** Per-app outcome shown after a force-stop operation, surfaced in the UI. */
data class StopResult(
    val packageName: String,
    val appName: String,
    val success: Boolean,
    val detail: String
)
