package com.example.pokeapi.features.teams.presentation

data class TeamListState(
    val teams: List<TeamSummary> = emptyList(),
    val isLoading: Boolean = false
)

data class TeamSummary(
    val id: Long,
    val name: String,
    val memberCount: Int
)
