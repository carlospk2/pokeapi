package com.example.kmm_jcmm_test.features.notes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmm_jcmm_test.shared.features.notes.domain.usecase.DeleteNoteUseCase
import com.example.kmm_jcmm_test.shared.features.notes.domain.usecase.GetNotesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val getNotes: GetNotesUseCase,
    private val deleteNote: DeleteNoteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NoteListState())
    val state: StateFlow<NoteListState> = _state.asStateFlow()

    private val _effect = Channel<NoteListEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        observeNotes()
    }

    fun onEvent(event: NoteListEvent) {
        when (event) {
            is NoteListEvent.DeleteNote -> viewModelScope.launch {
                deleteNote(event.note)
            }
            is NoteListEvent.NavigateToDetail -> viewModelScope.launch {
                _effect.send(NoteListEffect.NavigateToDetail(event.id))
            }
            NoteListEvent.NavigateToCreate -> viewModelScope.launch {
                _effect.send(NoteListEffect.NavigateToCreate)
            }
        }
    }

    private fun observeNotes() {
        getNotes()
            .onEach { notes -> _state.update { it.copy(notes = notes, isLoading = false) } }
            .launchIn(viewModelScope)
    }
}
