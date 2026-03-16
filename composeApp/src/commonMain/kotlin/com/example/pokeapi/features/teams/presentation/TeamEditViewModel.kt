package com.example.pokeapi.features.teams.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapi.shared.database.dao.PokemonDao
import com.example.pokeapi.shared.database.dao.TeamDao
import com.example.pokeapi.shared.database.dao.TeamMemberDao
import com.example.pokeapi.shared.database.entity.TeamEntity
import com.example.pokeapi.shared.database.entity.TeamMemberEntity
import com.example.pokeapi.shared.platform.currentTimeMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeamEditViewModel(
    private val teamId: Long?,
    private val teamDao: TeamDao,
    private val teamMemberDao: TeamMemberDao,
    private val pokemonDao: PokemonDao
) : ViewModel() {

    private val _state = MutableStateFlow(TeamEditState(teamId = teamId, isLoading = teamId != null))
    val state: StateFlow<TeamEditState> = _state.asStateFlow()

    private var currentTeamId: Long? = teamId

    init {
        if (teamId != null) {
            viewModelScope.launch {
                loadTeam(teamId)
            }
        } else {
            // Create a new team immediately so we have an ID for navigation
            viewModelScope.launch {
                val now = currentTimeMillis()
                val newId = teamDao.insert(TeamEntity(name = "My Team", createdAt = now, updatedAt = now))
                currentTeamId = newId
                _state.update { it.copy(teamId = newId, isLoading = false) }
            }
        }
    }

    private suspend fun loadTeam(id: Long) {
        val team = teamDao.getById(id) ?: return
        val members = teamMemberDao.getMembersForTeam(id)
        val displays = members.mapNotNull { member ->
            val pokemon = pokemonDao.getById(member.pokemonId) ?: return@mapNotNull null
            val moveCount = listOfNotNull(member.move1Id, member.move2Id, member.move3Id, member.move4Id).size
            TeamMemberDisplay(
                slot = member.slot,
                pokemonId = pokemon.id,
                pokemonName = pokemon.name,
                spriteUrl = pokemon.spriteUrl,
                spriteLocalPath = pokemon.spriteLocalPath,
                level = member.level,
                moveCount = moveCount,
                moveIds = listOfNotNull(member.move1Id, member.move2Id, member.move3Id, member.move4Id)
            )
        }
        _state.update { it.copy(teamId = id, teamName = team.name, members = displays, isLoading = false) }
    }

    fun updateName(name: String) {
        _state.update { it.copy(teamName = name) }
    }

    fun removeMember(slot: Int) {
        viewModelScope.launch {
            val id = currentTeamId ?: return@launch
            val members = teamMemberDao.getMembersForTeam(id)
            val toDelete = members.find { it.slot == slot }
            if (toDelete != null) {
                teamMemberDao.delete(toDelete)
            }
            loadTeam(id)
        }
    }

    fun saveTeam() {
        viewModelScope.launch {
            val id = currentTeamId ?: return@launch
            val existing = teamDao.getById(id) ?: return@launch
            val now = currentTimeMillis()
            teamDao.update(existing.copy(name = _state.value.teamName, updatedAt = now))
        }
    }
}
