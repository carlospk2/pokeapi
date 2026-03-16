@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.example.pokeapi.shared.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    val dbFilePath = requireNotNull(documentDirectory?.path) + "/app_database.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath
    )
}
