package ru.practicum.android.diploma.filtration.di.data

import org.koin.dsl.module
import ru.practicum.android.diploma.filtration.data.mapper.AreaMapper
import ru.practicum.android.diploma.filtration.data.repository.AreaRepositoryImplementation
import ru.practicum.android.diploma.filtration.data.repository.IndustriesRepositoryImpl
import ru.practicum.android.diploma.filtration.domain.repository.AreaRepository
import ru.practicum.android.diploma.filtration.domain.repository.IndustriesRepository

val filtrationDataModule = module {
    single<AreaRepository> {
        AreaRepositoryImplementation(get(), get())
    }

    single<IndustriesRepository> {
        IndustriesRepositoryImpl(
            networkUtil = get(),
            context = get(),
            networkClient = get()
        )
    }

    single<AreaMapper> { AreaMapper() }
}
