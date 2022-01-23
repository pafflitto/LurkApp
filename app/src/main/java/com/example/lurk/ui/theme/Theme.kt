package com.example.lurk.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

val DarkColorPalette = darkColorScheme(
    primary = RedDark,
    onPrimary = Smoke,
    secondary = PurpleDark,
    surface = Color.Black,
    onSurface = Color.White,
)

private val LightColorPalette = lightColorScheme(
    primary = RedLight,
    onPrimary = Color.Black,
    secondary = PurpleLight,

    /* Other default colors to override
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

object LurkButtonRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor(): Color = MaterialTheme.colorScheme.primary

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        Color.Red,
        lightTheme = !isSystemInDarkTheme(),
    )
}

@Composable
fun LurkTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
    ) {
        CompositionLocalProvider(
            LocalRippleTheme provides LurkButtonRippleTheme,
            content = content
        )
    }
}