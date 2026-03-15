package com.example.kmm_jcmm_test.shared.features.notes.domain.usecase

import com.example.kmm_jcmm_test.shared.features.notes.domain.model.Note
import com.example.kmm_jcmm_test.shared.features.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> = repository.getNotes()
}
