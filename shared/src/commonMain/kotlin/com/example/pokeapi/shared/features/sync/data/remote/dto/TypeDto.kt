package com.example.pokeapi.shared.features.sync.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TypeDto(
    val id: Int,
    val name: String,
    @SerialName("damage_relations") val damageRelations: DamageRelationsDto
)

@Serializable
data class DamageRelationsDto(
    @SerialName("double_damage_to") val doubleDamageTo: List<NamedApiResourceDto> = emptyList(),
    @SerialName("half_damage_to") val halfDamageTo: List<NamedApiResourceDto> = emptyList(),
    @SerialName("no_damage_to") val noDamageTo: List<NamedApiResourceDto> = emptyList()
)

@Serializable
data class NamedApiResourceDto(
    val name: String,
    val url: String = ""
)
