package ru.practicum.android.diploma.filtration.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.AppInteractor
import ru.practicum.android.diploma.core.domain.repository.StorageKey
import ru.practicum.android.diploma.filtration.domain.model.FilterStorage

class MainFilterViewModel(
    val appInteractor: AppInteractor
) : ViewModel() {

    private var latestSalary: String? = null
    private val _stateFilter = MutableStateFlow(value = FilterStorage())
    val stateFilter = _stateFilter.asStateFlow()

    init {
        viewModelScope.launch {
            appInteractor.getAllDataWithNames().collect {
                _stateFilter.value = it
            }
        }
    }

    fun clearByKey(key: StorageKey) {
        appInteractor.clearByKey(key = key)
    }

    fun clearStorage() {
        appInteractor.clearStorage()
    }

    fun saveSalary(salary: String) {
        val salary = salary.filter { it.isDigit() }
        if (salary.isNotEmpty()) {
            if (salary == latestSalary) {
                return
            }
            appInteractor.saveData(StorageKey.SALARY_NAME_KEY, salary)
        } else {
            appInteractor.clearByKey(StorageKey.SALARY_NAME_KEY)
        }
        latestSalary = salary
    }

    fun saveWithSalary(withSalary: Boolean) {
        if (withSalary) {
            appInteractor.saveData(StorageKey.ONLY_WITH_SALARY_KEY, true.toString())
        } else {
            appInteractor.clearByKey(StorageKey.ONLY_WITH_SALARY_KEY)
        }
    }
}
