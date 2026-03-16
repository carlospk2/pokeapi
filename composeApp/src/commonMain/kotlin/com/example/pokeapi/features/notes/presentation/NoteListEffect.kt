package com.example.pokeapi.features.notes.presentation

sealed interface NoteListEffect {
    data class NavigateToDetail(val id: Long) : NoteListEffect
    data object NavigateToCreate : NoteListEffect
    data class ShowSnackbar(val message: String) : NoteListEffect
}
