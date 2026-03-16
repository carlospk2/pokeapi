package com.example.pokeapi.shared.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trainers")
data class TrainerEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val difficultyTier: Int,
    val aiStrategy: String,
    val teamJson: String
)
