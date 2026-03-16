package com.example.pokeapi.shared.features.sync.domain.repository

import com.example.pokeapi.shared.features.sync.domain.model.SyncProgress
import kotlinx.coroutines.flow.Flow

interface SyncRepository {
    fun startSync(): Flow<SyncProgress>
    suspend fun isSyncCompleted(): Boolean
}
