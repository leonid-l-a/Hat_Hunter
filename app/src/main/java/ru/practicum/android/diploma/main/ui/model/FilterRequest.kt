package ru.practicum.android.diploma.main.ui.model

data class FilterRequest(
    val areaId: String? = null,
    val industryId: String? = null,
    val salaryId: String? = null,
    val withSalary: String? = null,
) {
    val isEmpty: Boolean
        get() = areaId == null && industryId == null && salaryId == null && withSalary == null
}
