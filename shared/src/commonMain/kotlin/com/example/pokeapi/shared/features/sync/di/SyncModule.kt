package com.example.pokeapi.shared.features.sync.di

import com.example.pokeapi.shared.features.sync.data.remote.PokemonApiService
import com.example.pokeapi.shared.features.sync.data.repository.SyncRepositoryImpl
import com.example.pokeapi.shared.features.sync.domain.repository.SyncRepository
import com.example.pokeapi.shared.features.sync.domain.usecase.GetSyncStatusUseCase
import com.example.pokeapi.shared.features.sync.domain.usecase.StartSyncUseCase
import org.koin.dsl.module

val syncModule = module {
    single { PokemonApiService(get()) }
    single<SyncRepository> {
        SyncRepositoryImpl(
            apiService = get(),
            pokemonDao = get(),
            moveDao = get(),
            pokemonMoveDao = get(),
            typeEffectivenessDao = get(),
            syncStatusDao = get(),
            trainerDao = get()
        )
    }
    factory { StartSyncUseCase(get()) }
    factory { GetSyncStatusUseCase(get()) }
}
