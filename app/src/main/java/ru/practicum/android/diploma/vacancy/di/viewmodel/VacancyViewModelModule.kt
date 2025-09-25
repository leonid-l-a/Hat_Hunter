package ru.practicum.android.diploma.vacancy.di.viewmodel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.vacancy.ui.viewmodel.VacancyViewModel

val vacancyViewModelModule = module {
    viewModel {
        VacancyViewModel(
            get(),
            get(),
            get(),
            get(),
        )
    }
}
