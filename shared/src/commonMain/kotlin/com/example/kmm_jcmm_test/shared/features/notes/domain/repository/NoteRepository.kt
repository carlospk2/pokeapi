package com.example.kmm_jcmm_test.shared.features.notes.domain.repository

import com.example.kmm_jcmm_test.shared.features.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun saveNote(note: Note)
    suspend fun deleteNote(note: Note)
}
