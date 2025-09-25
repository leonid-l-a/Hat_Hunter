package ru.practicum.android.diploma.core.data.dto

import ru.practicum.android.diploma.core.data.dto.vacancydetails.Address
import ru.practicum.android.diploma.core.data.dto.vacancydetails.Contacts
import ru.practicum.android.diploma.core.data.dto.vacancydetails.Employer
import ru.practicum.android.diploma.core.data.dto.vacancydetails.Employment
import ru.practicum.android.diploma.core.data.dto.vacancydetails.Experience
import ru.practicum.android.diploma.core.data.dto.vacancydetails.Salary
import ru.practicum.android.diploma.core.data.dto.vacancydetails.Schedule

data class VacancyDetailDto(
    val id: String,
    val name: String,
    val description: String,
    val salary: Salary,
    val address: Address,
    val experience: Experience,
    val schedule: Schedule,
    val employment: Employment,
    val contacts: Contacts,
    val employer: Employer,
    val area: FilterAreas,
    val skills: List<String>,
    val url: String,
    val industry: FilterIndustryDetail
)
