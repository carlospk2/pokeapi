package com.example.pokeapi.features.teams.di

import com.example.pokeapi.features.teams.presentation.MoveSelectorViewModel
import com.example.pokeapi.features.teams.presentation.PokemonSelectorViewModel
import com.example.pokeapi.features.teams.presentation.TeamEditViewModel
import com.example.pokeapi.features.teams.presentation.TeamListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val teamsModule = module {
    viewModelOf(::TeamListViewModel)
    viewModel { params -> TeamEditViewModel(params.get(), get(), get(), get()) }
    viewModel { params -> PokemonSelectorViewModel(params.get(), params.get(), get(), get()) }
    viewModel { params -> MoveSelectorViewModel(params.get(), params.get(), params.get(), get(), get()) }
}
