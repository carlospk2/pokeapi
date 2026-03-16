package com.example.pokeapi.shared.features.sync.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PokemonListDto(
    val count: Int,
    val results: List<NamedApiResourceDto>
)
