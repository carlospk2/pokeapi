package com.example.pokeapi.features.teams.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapi.features.pokedex.domain.model.PokemonMove
import com.example.pokeapi.shared.database.dao.MoveDao
import com.example.pokeapi.shared.database.dao.TeamMemberDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MoveSelectorState(
    val availableMoves: List<PokemonMove> = emptyList(),
    val selectedMoveIds: Set<Int> = emptySet()
)

class MoveSelectorViewModel(
    private val teamId: Long,
    private val slot: Int,
    private val pokemonId: Int,
    private val moveDao: MoveDao,
    private val teamMemberDao: TeamMemberDao
) : ViewModel() {

    private val _state = MutableStateFlow(MoveSelectorState())
    val state: StateFlow<MoveSelectorState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val moves = moveDao.getMovesByPokemon(pokemonId).first()
            val moveDomains = moves.map { m ->
                PokemonMove(id = m.id, name = m.name, type = m.type, category = m.category, power = m.power, accuracy = m.accuracy, pp = m.pp)
            }

            // Load existing selected moves
            val member = teamMemberDao.getMembersForTeam(teamId).find { it.slot == slot }
            val selected = if (member != null) {
                setOfNotNull(member.move1Id.takeIf { it != 0 }, member.move2Id, member.move3Id, member.move4Id)
            } else emptySet()

            _state.update { it.copy(availableMoves = moveDomains, selectedMoveIds = selected) }
        }
    }

    fun toggleMove(moveId: Int) {
        val current = _state.value.selectedMoveIds
        if (moveId in current) {
            _state.update { it.copy(selectedMoveIds = current - moveId) }
        } else if (current.size < 4) {
            _state.update { it.copy(selectedMoveIds = current + moveId) }
        }
    }

    fun saveMoves() {
        viewModelScope.launch {
            val member = teamMemberDao.getMembersForTeam(teamId).find { it.slot == slot } ?: return@launch
            val ids = _state.value.selectedMoveIds.toList()
            teamMemberDao.update(member.copy(
                move1Id = ids.getOrElse(0) { 0 },
                move2Id = ids.getOrNull(1),
                move3Id = ids.getOrNull(2),
                move4Id = ids.getOrNull(3)
            ))
        }
    }
}
