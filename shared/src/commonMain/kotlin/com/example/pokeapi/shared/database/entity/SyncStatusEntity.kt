package com.example.pokeapi.shared.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_status")
data class SyncStatusEntity(
    @PrimaryKey val id: Int = 1,
    val isCompleted: Boolean = false,
    val lastSyncedPokemonId: Int = 0,
    val lastSyncedMoveId: Int = 0
)
