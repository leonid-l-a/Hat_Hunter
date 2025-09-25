package ru.practicum.android.diploma.main.di.viewmodel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.main.ui.viewmodel.SearchVacancyViewModel

val mainViewModelModule = module {
    viewModel {
        SearchVacancyViewModel(
            get(),
            get(),
            get()
        )
    }
}
