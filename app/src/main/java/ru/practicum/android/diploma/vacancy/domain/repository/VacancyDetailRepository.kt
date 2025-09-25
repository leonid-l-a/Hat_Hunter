package ru.practicum.android.diploma.vacancy.domain.repository

import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail

interface VacancyDetailRepository {
    suspend fun getVacancyDetail(id: String): VacancyDetail
}
