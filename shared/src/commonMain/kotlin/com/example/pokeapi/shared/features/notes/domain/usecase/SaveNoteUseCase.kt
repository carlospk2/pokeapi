package com.example.pokeapi.shared.features.notes.domain.usecase

import com.example.pokeapi.shared.features.notes.domain.model.Note
import com.example.pokeapi.shared.features.notes.domain.repository.NoteRepository

class SaveNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) = repository.saveNote(note)
}
