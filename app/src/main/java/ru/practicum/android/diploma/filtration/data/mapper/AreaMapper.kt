package ru.practicum.android.diploma.filtration.data.mapper

import ru.practicum.android.diploma.core.data.dto.FilterAreasResponse
import ru.practicum.android.diploma.filtration.domain.model.Country
import ru.practicum.android.diploma.filtration.domain.model.Region

class AreaMapper {
    fun toCountriesAndRegions(response: FilterAreasResponse): Pair<List<Country>,
        Map<Int, List<Region>>> {
        val countries = mutableListOf<Country>()
        val regionsMap = mutableMapOf<Int, List<Region>>()

        response.areas.forEach { country ->
            countries.add(
                Country(
                    country.id,
                    country.name
                )
            )

            val regions = country.areas.map { region ->
                Region(
                    region.id,
                    region.name,
                    country.id
                )
            }

            regionsMap[country.id] = regions
        }

        return countries to regionsMap
    }
}
