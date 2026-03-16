package com.example.pokeapi.features.pokedex.presentation

import com.example.pokeapi.features.pokedex.domain.model.PokemonDetail

data class PokemonDetailState(
    val pokemon: PokemonDetail? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)
