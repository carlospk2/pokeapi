package com.example.pokeapi.features.pokedex.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapi.features.pokedex.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class PokedexViewModel(
    private val getPokemonList: GetPokemonListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PokedexState(isLoading = true))
    val state: StateFlow<PokedexState> = _state.asStateFlow()

    private val filterFlow = MutableStateFlow(Triple("", null as String?, null as Int?))

    init {
        filterFlow
            .flatMapLatest { (query, type, gen) ->
                getPokemonList(query, type, gen)
            }
            .onEach { list ->
                _state.update { it.copy(pokemon = list, isLoading = false, error = null) }
            }
            .catch { e ->
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: PokedexEvent) {
        when (event) {
            is PokedexEvent.SearchQueryChanged -> {
                _state.update { it.copy(searchQuery = event.query, selectedType = null, selectedGeneration = null) }
                filterFlow.value = Triple(event.query, null, null)
            }
            is PokedexEvent.TypeFilterSelected -> {
                _state.update { it.copy(selectedType = event.type, searchQuery = "", selectedGeneration = null) }
                filterFlow.value = Triple("", event.type, null)
            }
            is PokedexEvent.GenerationFilterSelected -> {
                _state.update { it.copy(selectedGeneration = event.generation, searchQuery = "", selectedType = null) }
                filterFlow.value = Triple("", null, event.generation)
            }
            is PokedexEvent.PokemonClicked -> Unit
        }
    }
}
