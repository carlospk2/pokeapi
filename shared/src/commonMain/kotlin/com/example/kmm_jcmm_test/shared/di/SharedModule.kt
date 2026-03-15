package com.example.kmm_jcmm_test.shared.di

import com.example.kmm_jcmm_test.shared.network.createHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

val sharedModule: Module = module {
    single { createHttpClient() }
}
