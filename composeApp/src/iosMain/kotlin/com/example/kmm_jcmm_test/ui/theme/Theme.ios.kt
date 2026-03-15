package com.example.kmm_jcmm_test.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

// iOS has no Material You / dynamic color system — always use static scheme
@Composable
actual fun colorScheme(darkTheme: Boolean): ColorScheme =
    if (darkTheme) DarkColorScheme else LightColorScheme
