package com.example.pokeapi.navigation

sealed interface Screen {

    // First screen if DB not synced
    data object Sync : Screen

    // Main menu — root after sync
    data object MainMenu : Screen

    // --- Pokédex ---
    data object Pokedex : Screen
    data class PokemonDetail(val pokemonId: Int) : Screen

    // --- Teams ---
    data object TeamList : Screen
    data class TeamEdit(val teamId: Long? = null) : Screen
    data class PokemonSelector(val teamId: Long, val slot: Int) : Screen
    data class MoveSelector(val teamId: Long, val slot: Int, val pokemonId: Int) : Screen

    // --- Battle ---
    data object TrainerSelect : Screen
    data class TeamSelectForBattle(val trainerId: Int) : Screen
    data class BattleVS(val trainerId: Int, val playerTeamId: Long) : Screen
    data class Battle(val trainerId: Int, val playerTeamId: Long) : Screen
    data class BattleResult(val battleRecordId: Long) : Screen

    // --- Records ---
    data object Records : Screen
    data class TrainerRecords(val trainerId: Int) : Screen
}
