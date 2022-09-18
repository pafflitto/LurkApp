package com.example.lurk.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.example.lurk.userPrefDataStore
import com.example.lurk.viewmodels.UserTheme
import com.google.android.material.color.ColorRoles
import com.google.android.material.color.MaterialColors

private val LightThemeColors = lightColorScheme(
	primary = md_theme_light_primary,
	onPrimary = md_theme_light_onPrimary,
	primaryContainer = md_theme_light_primaryContainer,
	onPrimaryContainer = md_theme_light_onPrimaryContainer,
	secondary = md_theme_light_secondary,
	onSecondary = md_theme_light_onSecondary,
	secondaryContainer = md_theme_light_secondaryContainer,
	onSecondaryContainer = md_theme_light_onSecondaryContainer,
	tertiary = md_theme_light_tertiary,
	onTertiary = md_theme_light_onTertiary,
	tertiaryContainer = md_theme_light_tertiaryContainer,
	onTertiaryContainer = md_theme_light_onTertiaryContainer,
	error = md_theme_light_error,
	errorContainer = md_theme_light_errorContainer,
	onError = md_theme_light_onError,
	onErrorContainer = md_theme_light_onErrorContainer,
	background = md_theme_light_background,
	onBackground = md_theme_light_onBackground,
	surface = md_theme_light_surface,
	onSurface = md_theme_light_onSurface,
	surfaceVariant = md_theme_light_surfaceVariant,
	onSurfaceVariant = md_theme_light_onSurfaceVariant,
	outline = md_theme_light_outline,
	inverseOnSurface = md_theme_light_inverseOnSurface,
	inverseSurface = md_theme_light_inverseSurface,
	inversePrimary = md_theme_light_inversePrimary,
)
private val DarkThemeColors = darkColorScheme(
	primary = md_theme_dark_primary,
	onPrimary = md_theme_dark_onPrimary,
	primaryContainer = md_theme_dark_primaryContainer,
	onPrimaryContainer = md_theme_dark_onPrimaryContainer,
	secondary = md_theme_dark_secondary,
	onSecondary = md_theme_dark_onSecondary,
	secondaryContainer = md_theme_dark_secondaryContainer,
	onSecondaryContainer = md_theme_dark_onSecondaryContainer,
	tertiary = md_theme_dark_tertiary,
	onTertiary = md_theme_dark_onTertiary,
	tertiaryContainer = md_theme_dark_tertiaryContainer,
	onTertiaryContainer = md_theme_dark_onTertiaryContainer,
	error = md_theme_dark_error,
	errorContainer = md_theme_dark_errorContainer,
	onError = md_theme_dark_onError,
	onErrorContainer = md_theme_dark_onErrorContainer,
	background = md_theme_dark_background,
	onBackground = md_theme_dark_onBackground,
	surface = md_theme_dark_surface,
	onSurface = md_theme_dark_onSurface,
	surfaceVariant = md_theme_dark_surfaceVariant,
	onSurfaceVariant = md_theme_dark_onSurfaceVariant,
	outline = md_theme_dark_outline,
	inverseOnSurface = md_theme_dark_inverseOnSurface,
	inverseSurface = md_theme_dark_inverseSurface,
	inversePrimary = md_theme_dark_inversePrimary,
)

data class CustomColor(val name:String, val color: Color, val harmonized: Boolean, var roles: ColorRoles)
data class ExtendedColors(val colors: Array<CustomColor>)


fun setupErrorColors(colorScheme: ColorScheme, isLight: Boolean): ColorScheme {
    val harmonizedError = MaterialColors.harmonize(error.toArgb(), colorScheme.primary.toArgb())
    val roles = MaterialColors.getColorRoles(harmonizedError, isLight)
    //returns a colorScheme with newly harmonized error colors
    return colorScheme.copy(
        error = Color(roles.accent),
        onError = Color(roles.onAccent),
        errorContainer = Color(roles.accentContainer),
        onErrorContainer = Color(roles.onAccentContainer)
    )
}

@Composable
fun LurkTheme(
	playerView: Boolean = false,
	useDarkPreviewTheme: Boolean? = null,
    content: @Composable () -> Unit
) {
	val context = LocalContext.current
	val preferredTheme by userPrefDataStore.userThemeFlow.collectAsState()

	val colors = when {
		useDarkPreviewTheme != null -> if (useDarkPreviewTheme) DarkThemeColors else LightThemeColors
		playerView -> if (preferredTheme == UserTheme.MaterialYou) dynamicDarkColorScheme(context) else DarkThemeColors
		else -> when (preferredTheme) {
			UserTheme.Auto -> if (isSystemInDarkTheme()) DarkThemeColors else LightThemeColors
			UserTheme.Dark -> DarkThemeColors
			UserTheme.Light -> LightThemeColors
			UserTheme.MaterialYou -> {
				if (isSystemInDarkTheme()) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
					context
				)
			}
		}
	}

	MaterialTheme(
		colorScheme = colors,
		typography = AppTypography,
		content = content
	)
}

object Extended {
	val PostBackgroundColor: Color
		@Composable
		get() {
			val theme by userPrefDataStore.userThemeFlow.collectAsState()
			return when (theme) {
				UserTheme.Dark -> Color.Black
				UserTheme.Light -> Color.White
				UserTheme.MaterialYou -> if (isSystemInDarkTheme()) Color.Black else Color.White
				UserTheme.Auto -> if (isSystemInDarkTheme()) Color.Black else Color.White
			}
		}
}