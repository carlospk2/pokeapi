package com.example.pokeapi.shared.platform

import android.content.Context
import java.io.File

private lateinit var appContext: Context

fun initFileSystem(context: Context) {
    appContext = context.applicationContext
}

actual fun getSpritesDirectory(): String {
    val dir = File(appContext.filesDir, "sprites")
    if (!dir.exists()) dir.mkdirs()
    return dir.absolutePath
}

actual fun saveFile(path: String, bytes: ByteArray) {
    val file = File(path)
    file.parentFile?.mkdirs()
    file.writeBytes(bytes)
}

actual fun fileExists(path: String): Boolean {
    return File(path).exists()
}
