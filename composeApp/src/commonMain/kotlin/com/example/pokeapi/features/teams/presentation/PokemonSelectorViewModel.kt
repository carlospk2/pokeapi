package com.example.pokeapi.features.teams.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapi.features.pokedex.domain.model.Pokemon
import com.example.pokeapi.shared.database.dao.PokemonDao
import com.example.pokeapi.shared.database.dao.TeamMemberDao
import com.example.pokeapi.shared.database.entity.PokemonEntity
import com.example.pokeapi.shared.database.entity.TeamMemberEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PokemonSelectorState(
    val pokemon: List<Pokemon> = emptyList(),
    val searchQuery: String = ""
)

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonSelectorViewModel(
    private val teamId: Long,
    private val slot: Int,
    private val pokemonDao: PokemonDao,
    private val teamMemberDao: TeamMemberDao
) : ViewModel() {

    private val _state = MutableStateFlow(PokemonSelectorState())
    val state: StateFlow<PokemonSelectorState> = _state.asStateFlow()

    private val searchQuery = MutableStateFlow("")

    init {
        searchQuery
            .flatMapLatest { query ->
                if (query.isBlank()) pokemonDao.getAll()
                else pokemonDao.searchByName(query)
            }
            .onEach { list ->
                _state.update { it.copy(pokemon = list.map { e -> e.toDomain() }) }
            }
            .launchIn(viewModelScope)
    }

    fun search(query: String) {
        _state.update { it.copy(searchQuery = query) }
        searchQuery.value = query
    }

    fun selectPokemon(pokemonId: Int) {
        viewModelScope.launch {
            // Delete any existing member at this exact (teamId, slot) — atomic, no ViewModel state dependency
            teamMemberDao.deleteBySlot(teamId, slot)
            teamMemberDao.insert(TeamMemberEntity(
                teamId = teamId,
                pokemonId = pokemonId,
                slot = slot,
                level = 50,
                move1Id = 0
            ))
        }
    }

    private fun PokemonEntity.toDomain() = Pokemon(
        id = id,
        name = name,
        spriteUrl = spriteUrl,
        spriteLocalPath = spriteLocalPath,
        typePrimary = typePrimary,
        typeSecondary = typeSecondary,
        generation = generation
    )
}
