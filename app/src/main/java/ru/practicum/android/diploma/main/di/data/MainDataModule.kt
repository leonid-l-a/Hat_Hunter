package ru.practicum.android.diploma.main.di.data

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.util.NetworkUtil

val mainDataModule = module {
    single<NetworkUtil> {
        NetworkUtil(androidContext())
    }
}
