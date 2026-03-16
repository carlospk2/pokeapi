package com.example.pokeapi.ui.theme

import androidx.compose.ui.graphics.Color

// Retro GBA Palette
val Background = Color(0xFF1A1F2E)
val Surface = Color(0xFF252D3D)
val SurfaceVariant = Color(0xFF2E3850)
val Primary = Color(0xFF3D6BE8)
val PrimaryVariant = Color(0xFF2952C2)
val OnBackground = Color(0xFFE8E8E8)
val OnSurface = Color(0xFFD0D0D0)
val OnPrimary = Color(0xFFFFFFFF)
val Secondary = Color(0xFF546E7A)
val Error = Color(0xFFE53935)
val Success = Color(0xFF4CAF50)
val Warning = Color(0xFFFFEB3B)

// Type Colors - Pokémon type palette
val TypeNormal = Color(0xFFA8A878)
val TypeFire = Color(0xFFF08030)
val TypeWater = Color(0xFF6890F0)
val TypeGrass = Color(0xFF78C850)
val TypeElectric = Color(0xFFF8D030)
val TypeIce = Color(0xFF98D8D8)
val TypeFighting = Color(0xFFC03028)
val TypePoison = Color(0xFFA040A0)
val TypeGround = Color(0xFFE0C068)
val TypeFlying = Color(0xFFA890F0)
val TypePsychic = Color(0xFFF85888)
val TypeBug = Color(0xFFA8B820)
val TypeRock = Color(0xFFB8A038)
val TypeGhost = Color(0xFF705898)
val TypeDragon = Color(0xFF7038F8)
val TypeDark = Color(0xFF705848)
val TypeSteel = Color(0xFFB8B8D0)
val TypeFairy = Color(0xFFEE99AC)

// HP Bar Colors
val HpHigh = Color(0xFF4CAF50)    // > 50%
val HpMedium = Color(0xFFFFEB3B)  // 25-50%
val HpLow = Color(0xFFF44336)     // < 25%

// Border highlight/shadow for retro effect
val BorderHighlight = Color(0xFF5A6880)
val BorderShadow = Color(0xFF0D1020)

// Legacy purple colors (kept for compatibility)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

fun typeColor(type: String): Color = when (type.lowercase()) {
    "normal" -> TypeNormal
    "fire" -> TypeFire
    "water" -> TypeWater
    "grass" -> TypeGrass
    "electric" -> TypeElectric
    "ice" -> TypeIce
    "fighting" -> TypeFighting
    "poison" -> TypePoison
    "ground" -> TypeGround
    "flying" -> TypeFlying
    "psychic" -> TypePsychic
    "bug" -> TypeBug
    "rock" -> TypeRock
    "ghost" -> TypeGhost
    "dragon" -> TypeDragon
    "dark" -> TypeDark
    "steel" -> TypeSteel
    "fairy" -> TypeFairy
    else -> TypeNormal
}
