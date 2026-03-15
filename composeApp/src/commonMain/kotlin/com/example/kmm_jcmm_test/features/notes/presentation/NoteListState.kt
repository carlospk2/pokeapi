package com.example.kmm_jcmm_test.features.notes.presentation

import com.example.kmm_jcmm_test.shared.features.notes.domain.model.Note

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
