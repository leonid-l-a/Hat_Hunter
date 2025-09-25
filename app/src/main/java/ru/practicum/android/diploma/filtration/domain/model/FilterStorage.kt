package ru.practicum.android.diploma.filtration.domain.model

data class FilterStorage(
    val regionId: String = "",
    val countryId: String = "",
    val areaId: String = "",
    val regionValue: String = "",
    val countryValue: String = "",
    val industryId: String = "",
    val industryValue: String = "",
    val salaryId: String = "",
    val salaryValue: String = "",
    val withSalary: String = "",
)

fun FilterStorage.hasActiveFilters(): Boolean {
    return countryId.isNotEmpty() ||
        regionId.isNotEmpty() ||
        regionValue.isNotEmpty() ||
        countryValue.isNotEmpty() ||
        salaryValue.isNotEmpty() ||
        industryValue.isNotEmpty() ||
        withSalary.isNotEmpty()
}
