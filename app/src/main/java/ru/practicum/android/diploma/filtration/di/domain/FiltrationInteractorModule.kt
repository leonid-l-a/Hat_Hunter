package ru.practicum.android.diploma.filtration.di.domain

import org.koin.dsl.module
import ru.practicum.android.diploma.filtration.domain.interactor.GetAreasUseCase
import ru.practicum.android.diploma.filtration.domain.interactor.GetAreasUseCaseImplementation
import ru.practicum.android.diploma.filtration.domain.interactor.IndustriesInteractor
import ru.practicum.android.diploma.filtration.domain.interactor.IndustriesInteractorImpl

val filtrationInteractorModule = module {
    factory<GetAreasUseCase> {
        GetAreasUseCaseImplementation(get())
    }

    single<IndustriesInteractor> {
        IndustriesInteractorImpl(get())
    }
}
