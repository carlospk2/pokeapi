package com.example.kmm_jcmm_test

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.kmm_jcmm_test.shared.getPlatform
import com.example.kmm_jcmm_test.ui.theme.KmmjcmmtestTheme

@Composable
fun App() {
    KmmjcmmtestTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Greeting(
                name     = getPlatform().name,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Box(
        modifier         = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Hello, $name!")
    }
}
