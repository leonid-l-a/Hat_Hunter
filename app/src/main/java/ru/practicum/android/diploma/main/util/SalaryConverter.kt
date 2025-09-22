package ru.practicum.android.diploma.main.util

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.providers.ResourceProvider
import ru.practicum.android.diploma.main.domain.model.SalaryMainData

fun SalaryMainData.getFormatSalary(provider: ResourceProvider): String {
    val formattedFrom = from?.let { formatNumber(it) } ?: ""
    val formattedTo = to?.let { formatNumber(it) } ?: ""
    val currency = currency?.let { " $it" } ?: ""

    return when {
        from != null && to != null -> provider.getString(
            R.string.salary_from_to,
            formattedFrom,
            formattedTo
        ) + currency

        from != null -> provider.getString(
            R.string.salary_from,
            formattedFrom
        ) + currency

        to != null -> provider.getString(
            R.string.salary_to,
            formattedTo
        ) + currency

        else -> provider.getString(R.string.salary_not_marked)
    }
}

private fun formatNumber(number: Int): String {
    return "%,d".format(number).replace(',', ' ')
}
