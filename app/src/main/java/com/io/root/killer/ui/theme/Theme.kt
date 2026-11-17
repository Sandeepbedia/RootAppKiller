package com.io.root.killer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Deliberately a single, always-dark scheme: a root/system utility like
 * this reads better as a dark "terminal-adjacent" tool, and a light
 * variant adds no real value here. [isSystemInDarkTheme] is still read so
 * nothing looks broken if a future light variant is added.
 */
private val DarkScheme = darkColorScheme(
    primary = AccentBlue,
    onPrimary = Color.Black,
    secondary = AccentAmber,
    error = AccentRed,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onBackground = OnSurfaceDark,
    onSurface = OnSurfaceDark,
    onSurfaceVariant = OnSurfaceMutedDark
)

@Composable
fun RootAppKillerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkScheme,
        typography = AppTypography,
        content = content
    )
}
