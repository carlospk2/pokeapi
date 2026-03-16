package com.example.pokeapi.features.trainers.di

import com.example.pokeapi.features.trainers.presentation.TrainerSelectViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val trainersModule = module {
    viewModelOf(::TrainerSelectViewModel)
}
