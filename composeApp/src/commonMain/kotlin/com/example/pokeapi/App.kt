package com.example.pokeapi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.pokeapi.navigation.Screen
import com.example.pokeapi.shared.getPlatform
import com.example.pokeapi.ui.theme.PokeapiTheme

@Composable
fun App() {
    PokeapiTheme {
        val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }

        NavDisplay(
            backStack = backStack,
            onBack = { if (backStack.size > 1) backStack.removeLastOrNull() }
        ) { screen ->
            when (screen) {
                Screen.Home -> NavEntry(screen) {
                    HomeScreen(onNavigateToDetail = { id -> backStack.add(Screen.Detail(id)) })
                }
                is Screen.Detail -> NavEntry(screen) {
                    DetailScreen(id = screen.id)
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(onNavigateToDetail: (String) -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Hello from ${getPlatform().name}!")
        }
    }
}

@Composable
private fun DetailScreen(id: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Detail: $id")
    }
}
