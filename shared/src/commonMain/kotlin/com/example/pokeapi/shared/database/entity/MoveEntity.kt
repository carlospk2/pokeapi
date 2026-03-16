package com.example.pokeapi.shared.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moves")
data class MoveEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val type: String,
    val category: String,
    val power: Int? = null,
    val accuracy: Int? = null,
    val pp: Int,
    val priority: Int
)
