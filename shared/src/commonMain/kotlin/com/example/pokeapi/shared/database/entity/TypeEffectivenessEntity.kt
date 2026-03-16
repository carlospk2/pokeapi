package com.example.pokeapi.shared.database.entity

import androidx.room.Entity

@Entity(
    tableName = "type_effectiveness",
    primaryKeys = ["attackingType", "defendingType"]
)
data class TypeEffectivenessEntity(
    val attackingType: String,
    val defendingType: String,
    val multiplier: Float
)
