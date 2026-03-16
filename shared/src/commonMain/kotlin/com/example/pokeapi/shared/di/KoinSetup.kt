package com.example.pokeapi.shared.di

import com.example.pokeapi.shared.features.notes.di.noteModule
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    extraModules: List<Module> = emptyList(),
    appDeclaration: KoinAppDeclaration = {}
) {
    GlobalContext.getOrNull() ?: startKoin {
        appDeclaration()
        modules(
            platformModule(),
            databaseModule,
            sharedModule,
            noteModule,
            *extraModules.toTypedArray()
        )
    }
}
