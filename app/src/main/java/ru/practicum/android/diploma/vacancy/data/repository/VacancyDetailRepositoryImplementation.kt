package ru.practicum.android.diploma.vacancy.data.repository

import ru.practicum.android.diploma.core.data.network.VacancyNetworkClient
import ru.practicum.android.diploma.vacancy.data.mapper.toDomain
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail
import ru.practicum.android.diploma.vacancy.domain.repository.VacancyDetailRepository

class VacancyDetailRepositoryImplementation(
    private val vacancyNetworkClient: VacancyNetworkClient
) : VacancyDetailRepository {
    override suspend fun getVacancyDetail(id: String): VacancyDetail {
        val dto = vacancyNetworkClient.getVacancy(id).vacancyDetailDto
        return dto.toDomain()
    }
}
