package ru.practicum.android.diploma.core.providers

interface ResourceProvider {
    fun getString(resId: Int): String
    fun getString(resId: Int, vararg args: Any): String
}
