package com.example.pokeapi.shared.features.sync.domain.usecase

import com.example.pokeapi.shared.features.sync.domain.repository.SyncRepository

class GetSyncStatusUseCase(private val syncRepository: SyncRepository) {
    suspend operator fun invoke(): Boolean = syncRepository.isSyncCompleted()
}
