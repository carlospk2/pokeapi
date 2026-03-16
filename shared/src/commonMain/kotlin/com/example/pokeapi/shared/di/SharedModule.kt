package com.example.pokeapi.shared.di

import com.example.pokeapi.shared.network.createHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

val sharedModule: Module = module {
    single { createHttpClient() }
}
