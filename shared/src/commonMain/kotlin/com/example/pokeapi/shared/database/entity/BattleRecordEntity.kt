package com.example.pokeapi.shared.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "battle_records")
data class BattleRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trainerId: Int,
    val playerTeamId: Long,
    val result: String,
    val turnsCount: Int,
    val date: Long,
    val pokemonRemaining: Int
)
