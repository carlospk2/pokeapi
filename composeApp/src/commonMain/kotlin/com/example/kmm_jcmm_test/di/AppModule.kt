package com.example.kmm_jcmm_test.di

import com.example.kmm_jcmm_test.features.notes.presentation.NoteListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Módulos de ViewModels para composeApp.
 * Se pasa como extraModules en initKoin() desde cada entry point de plataforma.
 */
val viewModelModules = listOf(
    module {
        viewModelOf(::NoteListViewModel)
    }
)
