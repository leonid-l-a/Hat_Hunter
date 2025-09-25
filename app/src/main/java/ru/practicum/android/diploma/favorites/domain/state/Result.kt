package ru.practicum.android.diploma.favorites.domain.state

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String?) : Result<Nothing>()
}
