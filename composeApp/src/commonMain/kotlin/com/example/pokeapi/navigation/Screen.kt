package com.example.pokeapi.navigation

sealed interface Screen {
    data object Home : Screen
    data class Detail(val id: String) : Screen
}
