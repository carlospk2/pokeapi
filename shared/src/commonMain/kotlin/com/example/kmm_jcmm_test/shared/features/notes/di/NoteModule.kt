package com.example.kmm_jcmm_test.shared.features.notes.di

import com.example.kmm_jcmm_test.shared.features.notes.data.repository.NoteRepositoryImpl
import com.example.kmm_jcmm_test.shared.features.notes.domain.repository.NoteRepository
import com.example.kmm_jcmm_test.shared.features.notes.domain.usecase.DeleteNoteUseCase
import com.example.kmm_jcmm_test.shared.features.notes.domain.usecase.GetNotesUseCase
import com.example.kmm_jcmm_test.shared.features.notes.domain.usecase.SaveNoteUseCase
import org.koin.dsl.module

val noteModule = module {
    // Data
    single<NoteRepository> { NoteRepositoryImpl(get()) }

    // Domain
    factory { GetNotesUseCase(get()) }
    factory { SaveNoteUseCase(get()) }
    factory { DeleteNoteUseCase(get()) }

    // Presentation — ViewModels se registran en composeApp via noteViewModelModule
}
