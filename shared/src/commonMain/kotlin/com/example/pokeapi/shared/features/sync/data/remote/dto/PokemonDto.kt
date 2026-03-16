package com.example.pokeapi.shared.features.sync.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDto(
    val id: Int,
    val name: String,
    val sprites: SpritesDto,
    val types: List<PokemonTypeSlotDto>,
    val stats: List<PokemonStatDto>,
    val moves: List<PokemonMoveSlotDto>
)

@Serializable
data class SpritesDto(
    @SerialName("front_default") val frontDefault: String? = null
)

@Serializable
data class PokemonTypeSlotDto(
    val slot: Int,
    val type: NamedApiResourceDto
)

@Serializable
data class PokemonStatDto(
    @SerialName("base_stat") val baseStat: Int,
    val stat: NamedApiResourceDto
)

@Serializable
data class PokemonMoveSlotDto(
    val move: NamedApiResourceDto
)
