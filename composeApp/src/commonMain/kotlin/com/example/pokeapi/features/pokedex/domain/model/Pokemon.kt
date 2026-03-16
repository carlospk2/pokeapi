package com.example.pokeapi.features.pokedex.domain.model

data class Pokemon(
    val id: Int,
    val name: String,
    val spriteUrl: String,
    val spriteLocalPath: String?,
    val typePrimary: String,
    val typeSecondary: String?,
    val generation: Int
)
