package com.expense.tracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val DarkColorScheme = darkColorScheme(
    primary = VolakoGreen,
    onPrimary = Color.Black,
    primaryContainer = VolakoGreenDark,
    onPrimaryContainer = Color.White,
    secondary = VolakoPurple,
    onSecondary = Color.White,
    error = VolakoRed,
    background = VolakoDarkSurface,
    onBackground = VolakoTextPrimaryDark,
    surface = VolakoDarkCard,
    onSurface = VolakoTextPrimaryDark,
    surfaceVariant = VolakoDarkElevated,
    onSurfaceVariant = VolakoTextSecondaryDark
)

val LightColorScheme = lightColorScheme(
    primary = VolakoGreen,
    onPrimary = Color.White,
    primaryContainer = VolakoGreenLight,
    onPrimaryContainer = Color(0xFF003828),
    secondary = VolakoPurple,
    onSecondary = Color.White,
    error = VolakoRed,
    background = VolakoLightBackground,
    onBackground = VolakoTextPrimaryLight,
    surface = VolakoLightSurface,
    onSurface = VolakoTextPrimaryLight,
    surfaceVariant = VolakoLightSurfaceVariant,
    onSurfaceVariant = VolakoTextSecondaryLight
)

@Composable
fun VolakoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}