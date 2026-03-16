package com.example.pokeapi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

val RetroColorScheme = darkColorScheme(
    background = Background,
    surface = Surface,
    surfaceVariant = SurfaceVariant,
    primary = Primary,
    onPrimary = OnPrimary,
    onBackground = OnBackground,
    onSurface = OnSurface,
    secondary = Secondary,
    error = Error
)

@Composable
fun PokeapiTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RetroColorScheme,
        typography = pokeTypography(),
        content = content
    )
}
