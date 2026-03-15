package com.example.kmm_jcmm_test.shared.core.common

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
