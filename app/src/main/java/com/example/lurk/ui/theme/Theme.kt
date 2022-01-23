package com.example.lurk.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = RedDark,
    onPrimary = Smoke,
    primaryVariant = Red,
    secondary = PurpleDark,
    secondaryVariant = Purple,
    surface = Color.Black,
    onSurface = Color.White,

)

private val LightColorPalette = lightColors(
    primary = RedLight,
    onPrimary = Color.Black,
    primaryVariant = Red,
    secondary = PurpleLight,
    secondaryVariant = Purple,
    surface = Color.White,
    onSurface = Color.Black

    /* Other default colors to override
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

object LurkButtonRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor(): Color = MaterialTheme.colors.secondaryVariant

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
        colors = colors,
        typography = Typography,
        shapes = Shapes
    ) {
        CompositionLocalProvider(
            LocalRippleTheme provides LurkButtonRippleTheme,
            content = content
        )
    }
}