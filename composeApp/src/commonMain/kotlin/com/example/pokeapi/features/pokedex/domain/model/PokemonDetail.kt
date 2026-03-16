package com.example.pokeapi.features.pokedex.domain.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val spriteUrl: String,
    val spriteLocalPath: String?,
    val typePrimary: String,
    val typeSecondary: String?,
    val baseHp: Int,
    val baseAttack: Int,
    val baseDefense: Int,
    val baseSpAttack: Int,
    val baseSpDefense: Int,
    val baseSpeed: Int,
    val generation: Int,
    val moves: List<PokemonMove>
)

data class PokemonMove(
    val id: Int,
    val name: String,
    val type: String,
    val category: String,
    val power: Int?,
    val accuracy: Int?,
    val pp: Int
)
