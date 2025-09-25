package ru.practicum.android.diploma.util

fun String?.getCurrencySymbolByDigitId(): String {
    return when (this) {
        "RUB" -> "₽"
        "USD" -> "$"
        "NZD" -> "NZ$"
        "EUR" -> "€"
        "SEK" -> "kr"
        "GBP" -> "£"
        "AUD" -> "AU$"
        "HKD" -> "HK$"
        "SGD" -> "SG$"
        else -> ""
    }
}
