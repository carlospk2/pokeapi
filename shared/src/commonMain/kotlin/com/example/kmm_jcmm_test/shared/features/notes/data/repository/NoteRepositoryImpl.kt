package com.example.kmm_jcmm_test.shared.features.notes.data.repository

import com.example.kmm_jcmm_test.shared.features.notes.data.local.dao.NoteDao
import com.example.kmm_jcmm_test.shared.features.notes.data.mapper.toDomain
import com.example.kmm_jcmm_test.shared.features.notes.data.mapper.toEntity
import com.example.kmm_jcmm_test.shared.features.notes.domain.model.Note
import com.example.kmm_jcmm_test.shared.features.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(private val dao: NoteDao) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> =
        dao.getAllNotes().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getNoteById(id: Long): Note? =
        dao.getNoteById(id)?.toDomain()

    override suspend fun saveNote(note: Note) =
        dao.upsert(note.toEntity())

    override suspend fun deleteNote(note: Note) =
        dao.delete(note.toEntity())
}
