package ru.practicum.android.diploma.vacancy.ui.state

import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail

sealed class VacancyState {
    object Loading : VacancyState()
    data class Success(val vacancyDetail: VacancyDetail, val isFavorite: Boolean) : VacancyState()
    object ServerError : VacancyState()
    object Error : VacancyState()
}
