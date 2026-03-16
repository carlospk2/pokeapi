package com.example.pokeapi.features.teams.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapi.shared.database.dao.PokemonDao
import com.example.pokeapi.shared.database.dao.TeamDao
import com.example.pokeapi.shared.database.dao.TeamMemberDao
import com.example.pokeapi.shared.database.entity.TeamEntity
import com.example.pokeapi.shared.platform.currentTimeMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeamEditViewModel(
    private val teamId: Long?,
    private val teamDao: TeamDao,
    private val teamMemberDao: TeamMemberDao,
    private val pokemonDao: PokemonDao
) : ViewModel() {

    private val _state = MutableStateFlow(TeamEditState(teamId = teamId, isLoading = true))
    val state: StateFlow<TeamEditState> = _state.asStateFlow()

    private var currentTeamId: Long? = teamId

    init {
        viewModelScope.launch {
            val id = teamId ?: run {
                val now = currentTimeMillis()
                val newId = teamDao.insert(TeamEntity(name = "My Team", createdAt = now, updatedAt = now))
                currentTeamId = newId
                newId
            }

            // Load team name once
            val team = teamDao.getById(id)
            _state.update { it.copy(teamId = id, teamName = team?.name ?: "My Team", isLoading = false) }

            // Observe members reactively — auto-updates whenever DB changes
            teamMemberDao.observeMembersForTeam(id)
                .onEach { members ->
                    val displays = members.mapNotNull { member ->
                        val pokemon = pokemonDao.getById(member.pokemonId) ?: return@mapNotNull null
                        TeamMemberDisplay(
                            slot = member.slot,
                            pokemonId = pokemon.id,
                            pokemonName = pokemon.name,
                            spriteUrl = pokemon.spriteUrl,
                            spriteLocalPath = pokemon.spriteLocalPath,
                            level = member.level,
                            moveCount = listOfNotNull(member.move1Id, member.move2Id, member.move3Id, member.move4Id).size,
                            moveIds = listOfNotNull(member.move1Id, member.move2Id, member.move3Id, member.move4Id)
                        )
                    }
                    _state.update { it.copy(members = displays) }
                }
                .launchIn(viewModelScope)
        }
    }

    fun updateName(name: String) {
        _state.update { it.copy(teamName = name) }
    }

    fun removeMember(slot: Int) {
        viewModelScope.launch {
            val id = currentTeamId ?: return@launch
            val members = teamMemberDao.getMembersForTeam(id)
            members.find { it.slot == slot }?.let { teamMemberDao.delete(it) }
            // No reload needed — observeMembersForTeam emits automatically
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
