package com.example.pokeapi.shared.features.sync.domain.usecase

import com.example.pokeapi.shared.features.sync.domain.model.SyncProgress
import com.example.pokeapi.shared.features.sync.domain.repository.SyncRepository
import kotlinx.coroutines.flow.Flow

class StartSyncUseCase(private val syncRepository: SyncRepository) {
    operator fun invoke(): Flow<SyncProgress> = syncRepository.startSync()
}
