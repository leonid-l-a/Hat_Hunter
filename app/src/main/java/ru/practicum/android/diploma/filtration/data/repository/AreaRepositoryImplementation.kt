package ru.practicum.android.diploma.filtration.data.repository

import ru.practicum.android.diploma.core.data.network.VacancyNetworkClient
import ru.practicum.android.diploma.filtration.data.mapper.AreaMapper
import ru.practicum.android.diploma.filtration.domain.model.Country
import ru.practicum.android.diploma.filtration.domain.model.Region
import ru.practicum.android.diploma.filtration.domain.repository.AreaRepository
import ru.practicum.android.diploma.filtration.domain.state.Result

class AreaRepositoryImplementation(
    private val vacancyNetworkClient: VacancyNetworkClient,
    private val mapper: AreaMapper,
) : AreaRepository {
    companion object {
        private const val SUCCESS_RESPONSE_CODE = 200
    }

    override suspend fun getAreas(): Result<Pair<List<Country>,
        Map<Int, List<Region>>>> {
        val response = vacancyNetworkClient.getFilterAreas()
        return if (response.resultCode == SUCCESS_RESPONSE_CODE) {
            val data = mapper.toCountriesAndRegions(response)
            Result.Success(data)
        } else {
            Result.Error("Ошибка сети (код: ${response.resultCode})")
        }
    }
}
