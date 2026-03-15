package com.example.kmm_jcmm_test.shared.features.notes.domain.usecase

import com.example.kmm_jcmm_test.shared.features.notes.domain.model.Note
import com.example.kmm_jcmm_test.shared.features.notes.domain.repository.NoteRepository

class DeleteNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) = repository.deleteNote(note)
}
