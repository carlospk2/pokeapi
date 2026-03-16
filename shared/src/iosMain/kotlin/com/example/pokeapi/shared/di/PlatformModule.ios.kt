package com.example.pokeapi.shared.di

import com.example.pokeapi.shared.database.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { getDatabaseBuilder() }
}
