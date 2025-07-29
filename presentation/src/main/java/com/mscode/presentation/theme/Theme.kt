package com.mscode.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    secondary = Secondary,
    background = Background,
    surface = Surface,
    error = Error,
    onPrimary = OnPrimary,
    onSecondary = OnSecondary,
    onBackground = OnBackground,
    onSurface = OnSurface,
    onError = OnError
)

private val DarkColorPalette = darkColors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    secondary = Secondary,
    background = Background,
    surface = Surface,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}