package com.example.pokeapi.shared.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val spriteUrl: String,
    val spriteLocalPath: String? = null,
    val typePrimary: String,
    val typeSecondary: String? = null,
    val baseHp: Int,
    val baseAttack: Int,
    val baseDefense: Int,
    val baseSpAttack: Int,
    val baseSpDefense: Int,
    val baseSpeed: Int,
    val generation: Int
)
