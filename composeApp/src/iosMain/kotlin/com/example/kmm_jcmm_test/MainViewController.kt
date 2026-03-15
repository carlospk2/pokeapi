package com.example.kmm_jcmm_test

import androidx.compose.ui.window.ComposeUIViewController
import com.example.kmm_jcmm_test.shared.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController { App() }
}
