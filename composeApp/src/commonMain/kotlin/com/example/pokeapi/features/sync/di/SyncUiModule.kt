package com.example.pokeapi.features.sync.di

import com.example.pokeapi.features.sync.presentation.SyncViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val syncUiModule = module {
    viewModelOf(::SyncViewModel)
}
