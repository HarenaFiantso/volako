package com.expense.tracker.common.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val MochaColorScheme =
    darkColorScheme(
        primary = MochaMauve,
        onPrimary = MochaBase,
        primaryContainer = MochaSurface0,
        onPrimaryContainer = MochaLavender,
        secondary = MochaBlue,
        onSecondary = MochaBase,
        secondaryContainer = MochaSurface1,
        onSecondaryContainer = MochaSapphire,
        tertiary = MochaPink,
        onTertiary = MochaBase,
        tertiaryContainer = MochaSurface0,
        onTertiaryContainer = MochaFlamingo,
        error = MochaRed,
        onError = MochaBase,
        errorContainer = MochaMaroon,
        onErrorContainer = MochaRosewater,
        background = MochaBase,
        onBackground = MochaText,
        surface = MochaSurface0,
        onSurface = MochaText,
        surfaceVariant = MochaSurface1,
        onSurfaceVariant = MochaSubtext1,
        outline = MochaOverlay1,
        outlineVariant = MochaSurface2,
        inverseSurface = MochaText,
        inverseOnSurface = MochaBase,
        inversePrimary = MochaMauve,
        scrim = MochaCrust,
    )

@Composable
fun VolakoTheme(content: @Composable () -> Unit) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = MochaBase.toArgb()
            window.navigationBarColor = MochaBase.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = MochaColorScheme,
        typography = Typography,
        content = content,
    )
}
