package com.example.pokeapi.features.pokedex.presentation

import com.example.pokeapi.features.pokedex.domain.model.Pokemon

data class PokedexState(
    val pokemon: List<Pokemon> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedType: String? = null,
    val selectedGeneration: Int? = null,
    val error: String? = null
)
