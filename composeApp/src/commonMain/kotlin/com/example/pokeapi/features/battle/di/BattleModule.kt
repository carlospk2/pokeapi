package com.example.pokeapi.features.battle.di

import com.example.pokeapi.features.battle.presentation.BattleViewModel
import com.example.pokeapi.features.battle.presentation.TeamSelectForBattleViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val battleModule = module {
    viewModelOf(::TeamSelectForBattleViewModel)
    viewModel { params ->
        BattleViewModel(
            trainerId = params.get(),
            playerTeamId = params.get(),
            trainerDao = get(),
            teamMemberDao = get(),
            pokemonDao = get(),
            moveDao = get(),
            typeEffectivenessDao = get(),
            battleRecordDao = get(),
            playerStatsDao = get()
        )
    }
}
