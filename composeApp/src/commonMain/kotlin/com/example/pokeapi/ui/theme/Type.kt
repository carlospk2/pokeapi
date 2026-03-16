package com.example.pokeapi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import pokeapi.composeapp.generated.resources.PressStart2P_Regular
import pokeapi.composeapp.generated.resources.Res

val PressStart2PFamily: FontFamily
    @androidx.compose.runtime.Composable
    get() = FontFamily(
        Font(Res.font.PressStart2P_Regular, weight = FontWeight.Normal)
    )

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

@androidx.compose.runtime.Composable
fun pokeTypography(): Typography {
    val pixelFont = PressStart2PFamily
    return Typography(
        displayLarge = TextStyle(fontFamily = pixelFont, fontSize = 16.sp, lineHeight = 24.sp),
        displayMedium = TextStyle(fontFamily = pixelFont, fontSize = 14.sp, lineHeight = 22.sp),
        displaySmall = TextStyle(fontFamily = pixelFont, fontSize = 12.sp, lineHeight = 20.sp),
        headlineLarge = TextStyle(fontFamily = pixelFont, fontSize = 14.sp, lineHeight = 22.sp),
        headlineMedium = TextStyle(fontFamily = pixelFont, fontSize = 12.sp, lineHeight = 20.sp),
        headlineSmall = TextStyle(fontFamily = pixelFont, fontSize = 10.sp, lineHeight = 18.sp),
        titleLarge = TextStyle(fontFamily = pixelFont, fontSize = 10.sp, lineHeight = 18.sp),
        titleMedium = TextStyle(fontFamily = pixelFont, fontSize = 8.sp, lineHeight = 16.sp),
        titleSmall = TextStyle(fontFamily = pixelFont, fontSize = 8.sp, lineHeight = 16.sp),
        bodyLarge = TextStyle(fontFamily = pixelFont, fontSize = 8.sp, lineHeight = 16.sp),
        bodyMedium = TextStyle(fontFamily = pixelFont, fontSize = 8.sp, lineHeight = 16.sp),
        bodySmall = TextStyle(fontFamily = pixelFont, fontSize = 6.sp, lineHeight = 12.sp),
        labelLarge = TextStyle(fontFamily = pixelFont, fontSize = 8.sp, lineHeight = 16.sp),
        labelMedium = TextStyle(fontFamily = pixelFont, fontSize = 6.sp, lineHeight = 12.sp),
        labelSmall = TextStyle(fontFamily = pixelFont, fontSize = 6.sp, lineHeight = 12.sp)
    )
}
