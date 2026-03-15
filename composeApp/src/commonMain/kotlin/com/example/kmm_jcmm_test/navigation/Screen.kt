package com.example.kmm_jcmm_test.navigation

sealed interface Screen {
    data object Home : Screen
    data class Detail(val id: String) : Screen
}
