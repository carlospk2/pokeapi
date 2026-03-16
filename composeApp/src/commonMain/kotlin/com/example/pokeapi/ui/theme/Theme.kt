package com.example.pokeapi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val DarkColorScheme = darkColorScheme(
    primary   = Purple80,
    secondary = PurpleGrey80,
    tertiary  = Pink80
)

val LightColorScheme = lightColorScheme(
    primary   = Purple40,
    secondary = PurpleGrey40,
    tertiary  = Pink40
)

/**
 * Returns the platform-specific color scheme.
 * Android: Material You dynamic color on API 31+, static fallback otherwise.
 * iOS: always returns static color scheme (no Material You equivalent).
 */
@Composable
expect fun colorScheme(darkTheme: Boolean): ColorScheme

@Composable
fun PokeapiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme(darkTheme),
        typography  = Typography,
        content     = content
    )
}
