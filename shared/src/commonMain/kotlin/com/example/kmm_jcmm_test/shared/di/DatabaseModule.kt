package com.example.kmm_jcmm_test.shared.di

import com.example.kmm_jcmm_test.shared.database.AppDatabase
import com.example.kmm_jcmm_test.shared.database.createDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { createDatabase(get()) }
    single { get<AppDatabase>().noteDao() }
}
