package com.example.pokeapi.shared.di

import com.example.pokeapi.shared.database.AppDatabase
import com.example.pokeapi.shared.database.createDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { createDatabase(get()) }
    single { get<AppDatabase>().pokemonDao() }
    single { get<AppDatabase>().moveDao() }
    single { get<AppDatabase>().pokemonMoveDao() }
    single { get<AppDatabase>().typeEffectivenessDao() }
    single { get<AppDatabase>().teamDao() }
    single { get<AppDatabase>().teamMemberDao() }
    single { get<AppDatabase>().trainerDao() }
    single { get<AppDatabase>().battleRecordDao() }
    single { get<AppDatabase>().playerStatsDao() }
    single { get<AppDatabase>().syncStatusDao() }
}
