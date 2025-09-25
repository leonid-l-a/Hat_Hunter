package ru.practicum.android.diploma.core.di.providers

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.core.providers.ResourceProvider
import ru.practicum.android.diploma.core.providers.ResourceProviderImpl

val coreProviderModule = module {
    single<ResourceProvider> {
        ResourceProviderImpl(context = androidContext())
    }
}
