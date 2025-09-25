package ru.practicum.android.diploma.core

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.core.di.data.coreDataModule
import ru.practicum.android.diploma.core.di.domain.coreInteractorModule
import ru.practicum.android.diploma.core.di.domain.coreRepositoryModule
import ru.practicum.android.diploma.core.di.providers.coreProviderModule
import ru.practicum.android.diploma.core.di.viewmodel.coreViewModelModule
import ru.practicum.android.diploma.favorites.di.data.favoriteDataModule
import ru.practicum.android.diploma.favorites.di.domain.favoriteInteractorModule
import ru.practicum.android.diploma.favorites.di.domain.favoriteRepositoryModule
import ru.practicum.android.diploma.favorites.di.viewmodel.favoriteViewModelModule
import ru.practicum.android.diploma.filtration.di.data.filtrationDataModule
import ru.practicum.android.diploma.filtration.di.domain.filtrationInteractorModule
import ru.practicum.android.diploma.filtration.di.viewmodel.filtrationViewModelModule
import ru.practicum.android.diploma.main.di.data.mainDataModule
import ru.practicum.android.diploma.main.di.domain.mainInteractorModule
import ru.practicum.android.diploma.main.di.domain.mainRepositoryModule
import ru.practicum.android.diploma.main.di.viewmodel.mainViewModelModule
import ru.practicum.android.diploma.vacancy.di.data.vacancyDataModule
import ru.practicum.android.diploma.vacancy.di.domain.vacancyInteractorModule
import ru.practicum.android.diploma.vacancy.di.viewmodel.vacancyViewModelModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                coreDataModule,
                coreRepositoryModule,
                coreInteractorModule,
                coreViewModelModule,
                mainInteractorModule,
                mainRepositoryModule,
                mainViewModelModule,
                mainDataModule,
                vacancyViewModelModule,
                vacancyDataModule,
                vacancyInteractorModule,
                favoriteViewModelModule,
                favoriteDataModule,
                favoriteInteractorModule,
                favoriteRepositoryModule,
                filtrationDataModule,
                filtrationInteractorModule,
                filtrationViewModelModule,
                coreProviderModule,
            )
        }
    }
}
