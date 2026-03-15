package com.example.kmm_jcmm_test.shared.features.notes.data.mapper

import com.example.kmm_jcmm_test.shared.features.notes.data.local.entity.NoteEntity
import com.example.kmm_jcmm_test.shared.features.notes.domain.model.Note

fun NoteEntity.toDomain(): Note = Note(id = id, title = title, content = content)

fun Note.toEntity(): NoteEntity = NoteEntity(id = id, title = title, content = content)
