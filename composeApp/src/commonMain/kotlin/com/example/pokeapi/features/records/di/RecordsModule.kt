package com.example.pokeapi.features.records.di

import com.example.pokeapi.features.records.presentation.RecordsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val recordsModule = module {
    viewModelOf(::RecordsViewModel)
}
