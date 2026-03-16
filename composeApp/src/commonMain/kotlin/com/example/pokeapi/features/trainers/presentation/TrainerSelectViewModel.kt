package com.example.pokeapi.features.trainers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapi.shared.database.dao.TrainerDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class TrainerDisplay(
    val id: Int,
    val name: String,
    val difficultyTier: Int,
    val aiStrategy: String
)

data class TrainerSelectState(
    val trainers: List<TrainerDisplay> = emptyList(),
    val isLoading: Boolean = true
)

class TrainerSelectViewModel(
    private val trainerDao: TrainerDao
) : ViewModel() {

    private val _state = MutableStateFlow(TrainerSelectState())
    val state: StateFlow<TrainerSelectState> = _state.asStateFlow()

    init {
        trainerDao.getAll()
            .onEach { trainers ->
                _state.update {
                    it.copy(
                        trainers = trainers.map { t ->
                            TrainerDisplay(
                                id = t.id,
                                name = t.name,
                                difficultyTier = t.difficultyTier,
                                aiStrategy = t.aiStrategy
                            )
                        },
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}
