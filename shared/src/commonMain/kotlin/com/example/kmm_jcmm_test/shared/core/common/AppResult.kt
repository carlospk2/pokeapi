package com.example.kmm_jcmm_test.shared.core.common

sealed interface AppResult<out D, out E : DataError> {
    data class Success<out D>(val data: D) : AppResult<D, Nothing>
    data class Error<out E : DataError>(val error: E) : AppResult<Nothing, E>
}

inline fun <D, E : DataError> AppResult<D, E>.onSuccess(action: (D) -> Unit): AppResult<D, E> {
    if (this is AppResult.Success) action(data)
    return this
}

inline fun <D, E : DataError> AppResult<D, E>.onError(action: (E) -> Unit): AppResult<D, E> {
    if (this is AppResult.Error) action(error)
    return this
}
