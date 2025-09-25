package ru.practicum.android.diploma.core.data.dto

data class Vacancy(
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: List<VacancyDetailDto>
)
