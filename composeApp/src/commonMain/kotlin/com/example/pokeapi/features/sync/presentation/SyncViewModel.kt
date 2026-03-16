package com.example.pokeapi.features.sync.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapi.shared.features.sync.domain.model.SyncProgress
import com.example.pokeapi.shared.features.sync.domain.usecase.StartSyncUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SyncViewModel(
    private val startSync: StartSyncUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SyncState())
    val state: StateFlow<SyncState> = _state.asStateFlow()

    init {
        startSyncFlow()
    }

    private fun startSyncFlow() {
        startSync()
            .onEach { progress ->
                when (progress) {
                    is SyncProgress.InProgress -> {
                        _state.update {
                            it.copy(
                                progress = progress,
                                phase = progress.phase,
                                current = progress.current,
                                total = progress.total,
                                error = null
                            )
                        }
                    }
                    is SyncProgress.Completed -> {
                        _state.update { it.copy(isCompleted = true, error = null) }
                    }
                    is SyncProgress.Error -> {
                        _state.update { it.copy(error = progress.message) }
                    }
                    SyncProgress.Idle -> Unit
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: SyncEvent) {
        when (event) {
            SyncEvent.StartSync, SyncEvent.RetrySync -> {
                _state.update { SyncState() }
                startSyncFlow()
            }
        }
    }
}
