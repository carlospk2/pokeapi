package com.example.pokeapi

import androidx.compose.ui.window.ComposeUIViewController
import com.example.pokeapi.di.viewModelModules
import com.example.pokeapi.shared.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin(extraModules = viewModelModules)
    return ComposeUIViewController { App() }
}
