package com.example.pokeapi.features.notes.presentation

import com.example.pokeapi.shared.features.notes.domain.model.Note

sealed interface NoteListEvent {
    data class DeleteNote(val note: Note) : NoteListEvent
    data class NavigateToDetail(val id: Long) : NoteListEvent
    data object NavigateToCreate : NoteListEvent
}
