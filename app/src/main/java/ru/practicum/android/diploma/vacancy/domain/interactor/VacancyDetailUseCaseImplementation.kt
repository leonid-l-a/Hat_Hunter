package ru.practicum.android.diploma.vacancy.domain.interactor

import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail
import ru.practicum.android.diploma.vacancy.domain.repository.VacancyDetailRepository

class VacancyDetailUseCaseImplementation(
    private val vacancyDetailRepository: VacancyDetailRepository
) : VacancyDetailUseCase {
    override suspend fun getVacancyDetail(id: String): VacancyDetail {
        val data = vacancyDetailRepository.getVacancyDetail(id)
        return data
    }
}
