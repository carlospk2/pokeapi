package com.example.pokeapi.shared.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_stats")
data class PlayerStatsEntity(
    @PrimaryKey val id: Int = 1,
    val totalWins: Int = 0,
    val totalLosses: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val totalBattles: Int = 0
)
