package ru.practicum.android.diploma.core.providers

import android.content.Context

class ResourceProviderImpl(private val context: Context) : ResourceProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg args: Any): String {
        return context.getString(resId, *args)
    }
}
