package com.example.pokeapi.features.teams.presentation

data class TeamEditState(
    val teamId: Long? = null,
    val teamName: String = "My Team",
    val members: List<TeamMemberDisplay> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class TeamMemberDisplay(
    val slot: Int,
    val pokemonId: Int,
    val pokemonName: String,
    val spriteUrl: String,
    val spriteLocalPath: String?,
    val level: Int,
    val moveCount: Int,
    val moveIds: List<Int>
)
