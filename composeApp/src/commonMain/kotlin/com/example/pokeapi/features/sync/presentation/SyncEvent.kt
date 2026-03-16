package com.example.pokeapi.features.sync.presentation

sealed interface SyncEvent {
    data object StartSync : SyncEvent
    data object RetrySync : SyncEvent
}
