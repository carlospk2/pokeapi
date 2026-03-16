package com.example.pokeapi.shared.features.sync.domain.model

sealed interface SyncProgress {
    data object Idle : SyncProgress
    data class InProgress(
        val phase: SyncPhase,
        val current: Int,
        val total: Int
    ) : SyncProgress
    data object Completed : SyncProgress
    data class Error(val message: String) : SyncProgress
}

enum class SyncPhase {
    TYPES, POKEMON, MOVES, SPRITES, TRAINERS
}
