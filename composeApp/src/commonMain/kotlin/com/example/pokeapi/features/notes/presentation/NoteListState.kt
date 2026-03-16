package com.example.pokeapi.features.notes.presentation

import com.example.pokeapi.shared.features.notes.domain.model.Note

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
