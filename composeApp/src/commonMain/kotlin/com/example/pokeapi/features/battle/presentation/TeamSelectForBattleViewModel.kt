package com.example.pokeapi.features.battle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapi.features.teams.presentation.TeamSummary
import com.example.pokeapi.shared.database.dao.TeamDao
import com.example.pokeapi.shared.database.dao.TeamMemberDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class TeamSelectForBattleState(
    val teams: List<TeamSummary> = emptyList()
)

class TeamSelectForBattleViewModel(
    private val teamDao: TeamDao,
    private val teamMemberDao: TeamMemberDao
) : ViewModel() {

    private val _state = MutableStateFlow(TeamSelectForBattleState())
    val state: StateFlow<TeamSelectForBattleState> = _state.asStateFlow()

    init {
        teamDao.getAll()
            .onEach { teams ->
                val summaries = teams.map { team ->
                    val members = teamMemberDao.getMembersForTeam(team.id)
                    TeamSummary(id = team.id, name = team.name, memberCount = members.size)
                }
                _state.update { it.copy(teams = summaries) }
            }
            .launchIn(viewModelScope)
    }
}
