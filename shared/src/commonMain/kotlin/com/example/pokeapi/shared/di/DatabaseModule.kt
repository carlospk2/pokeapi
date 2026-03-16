package com.example.pokeapi.shared.di

import com.example.pokeapi.shared.database.AppDatabase
import com.example.pokeapi.shared.database.createDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { createDatabase(get()) }
    single { get<AppDatabase>().noteDao() }
}
