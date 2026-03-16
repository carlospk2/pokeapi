package com.example.pokeapi.shared.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.pokeapi.shared.database.entity.SyncStatusEntity

@Dao
interface SyncStatusDao {

    @Query("SELECT * FROM sync_status WHERE id = 1")
    suspend fun get(): SyncStatusEntity?

    @Upsert
    suspend fun upsert(syncStatus: SyncStatusEntity)
}
