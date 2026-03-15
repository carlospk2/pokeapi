package com.example.kmm_jcmm_test.shared.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(entities = [NoteEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

// KSP genera la implementación `actual` para cada target
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
