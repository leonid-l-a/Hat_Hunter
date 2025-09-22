package ru.practicum.android.diploma.filtration.di.domain

import org.koin.dsl.module
import ru.practicum.android.diploma.filtration.domain.interactor.GetAreasUseCase
import ru.practicum.android.diploma.filtration.domain.interactor.GetAreasUseCaseImplementation
import ru.practicum.android.diploma.filtration.domain.interactor.IndustriesInteractor
import ru.practicum.android.diploma.filtration.domain.interactor.IndustriesInteractorImpl
import ru.practicum.android.diploma.filtration.data.mapper.AreaMapper

val filtrationInteractorModule = module {
    factory<GetAreasUseCase> {
        GetAreasUseCaseImplementation(get(), get())
    }

    single<IndustriesInteractor> {
        IndustriesInteractorImpl(get())
    }
    single<AreaMapper> { AreaMapper() }
}
