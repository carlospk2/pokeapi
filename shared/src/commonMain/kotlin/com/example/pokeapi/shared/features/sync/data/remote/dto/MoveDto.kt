package com.example.pokeapi.shared.features.sync.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoveDto(
    val id: Int,
    val name: String,
    val type: NamedApiResourceDto,
    @SerialName("damage_class") val damageClass: NamedApiResourceDto,
    val power: Int? = null,
    val accuracy: Int? = null,
    val pp: Int,
    val priority: Int = 0
)
