package com.example.pokeapi.shared.platform

expect fun getSpritesDirectory(): String

expect fun saveFile(path: String, bytes: ByteArray)

expect fun fileExists(path: String): Boolean
