package com.example.kmm_jcmm_test.shared.di

import com.example.kmm_jcmm_test.shared.database.getDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { getDatabaseBuilder(androidContext()) }
}
