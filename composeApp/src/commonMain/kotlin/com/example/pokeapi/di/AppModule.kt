package com.example.pokeapi.di

import com.example.pokeapi.features.battle.di.battleModule
import com.example.pokeapi.features.pokedex.di.pokedexModule
import com.example.pokeapi.features.records.di.recordsModule
import com.example.pokeapi.features.sync.di.syncUiModule
import com.example.pokeapi.features.teams.di.teamsModule
import com.example.pokeapi.features.trainers.di.trainersModule
import org.koin.core.module.Module

/**
 * All composeApp ViewModel modules.
 * Passed as extraModules to initKoin() from each platform entry point.
 */
val viewModelModules: List<Module> = listOf(
    syncUiModule,
    pokedexModule,
    teamsModule,
    trainersModule,
    battleModule,
    recordsModule
)
