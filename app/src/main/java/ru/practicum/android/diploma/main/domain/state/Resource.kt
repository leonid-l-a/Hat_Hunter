package ru.practicum.android.diploma.main.domain.state

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val isLazyError: Boolean = false) : Resource<T>()
}
