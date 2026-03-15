package com.example.kmm_jcmm_test.shared.di

import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    GlobalContext.getOrNull() ?: startKoin {
        appDeclaration()
        modules(
            platformModule(),
            databaseModule,
            sharedModule
        )
    }
}
