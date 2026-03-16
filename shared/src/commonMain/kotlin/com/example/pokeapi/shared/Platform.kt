package com.example.pokeapi.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
