package ru.practicum.android.diploma.filtration.domain.interactor

import ru.practicum.android.diploma.filtration.domain.model.Country
import ru.practicum.android.diploma.filtration.domain.model.Region
import ru.practicum.android.diploma.filtration.domain.repository.AreaRepository
import ru.practicum.android.diploma.filtration.domain.state.Result

class GetAreasUseCaseImplementation(
    private val repository: AreaRepository
) : GetAreasUseCase {
    override suspend fun invoke(): Result<Pair<List<Country>,
        Map<Int, List<Region>>>> {
        return when (
            val result = repository.getAreas()
        ) {
            is Result.Error -> result

            is Result.Success -> result
        }
    }
}
