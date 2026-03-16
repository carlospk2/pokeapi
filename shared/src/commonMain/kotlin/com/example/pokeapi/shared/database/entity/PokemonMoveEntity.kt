package com.example.pokeapi.shared.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "pokemon_moves",
    primaryKeys = ["pokemonId", "moveId"],
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = ["id"],
            childColumns = ["pokemonId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("pokemonId"), Index("moveId")]
)
data class PokemonMoveEntity(
    val pokemonId: Int,
    val moveId: Int
)
