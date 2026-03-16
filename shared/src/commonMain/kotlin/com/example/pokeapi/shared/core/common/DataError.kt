package com.example.pokeapi.shared.core.common

sealed interface DataError {
    enum class Network : DataError {
        NO_INTERNET,
        SERVER_ERROR,
        TIMEOUT,
        UNAUTHORIZED,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL,
        NOT_FOUND,
        UNKNOWN
    }
}
