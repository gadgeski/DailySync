// ui/theme/Theme.kt

@file:Suppress("ktlint:standard:function-naming")

package com.example.dailysync.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ★ "Sunset Lounge" Dark Theme
private val DarkColorScheme = darkColorScheme(
    primary = LuxuryGold,
    onPrimary = LuxuryDeepBrown,
    // ゴールドボタンの上の文字は濃い茶色

    secondary = LuxuryCopper,
    onSecondary = Color.White,

    tertiary = LuxuryChampagne,
    onTertiary = LuxuryDeepBrown,

    background = LuxuryDeepBrown,
    onBackground = LuxuryTextPrimary,

    surface = LuxuryGlassSurface,
    onSurface = LuxuryTextPrimary,

    error = Color(0xFFE57373),
)

// Soft-Bento Light Theme (Legacy)
private val LightColorScheme = lightColorScheme(
    primary = DailySyncPrimary,
    onPrimary = Color.White,
    secondary = DailySyncPrimary,
    tertiary = DailySyncOnPrimary,
    background = DailySyncBackgroundLight,
    surface = DailySyncSurfaceLight,
    onBackground = DailySyncOnBackgroundLight,
    onSurface = DailySyncOnSurfaceLight,
)

@Composable
fun DailySyncTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme =
        if (dynamicColor) {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        // ★ 新しいフォント定義を適用！
        content = content,
    )
}
