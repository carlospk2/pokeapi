package com.example.pokeapi.features.sync.presentation

import com.example.pokeapi.shared.features.sync.domain.model.SyncPhase
import com.example.pokeapi.shared.features.sync.domain.model.SyncProgress

data class SyncState(
    val progress: SyncProgress = SyncProgress.Idle,
    val phase: SyncPhase? = null,
    val current: Int = 0,
    val total: Int = 0,
    val isCompleted: Boolean = false,
    val error: String? = null
)
