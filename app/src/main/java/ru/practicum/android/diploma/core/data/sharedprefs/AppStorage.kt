package ru.practicum.android.diploma.core.data.sharedprefs

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.domain.repository.StorageKey
import ru.practicum.android.diploma.filtration.domain.model.FilterStorage

class AppStorage(
    val sharedPrefs: SharedPreferences,
) {
    private var _storageState = MutableStateFlow(FilterStorage())

    private fun getMapWithKeys(): Map<String, String?> {
        val country = getStorageByKey(StorageKey.COUNTRY_ID_KEY)
        val region = getStorageByKey(StorageKey.REGION_ID_KEY)
        val area = if (region.isNullOrBlank()) country else region
        val salary = getStorageByKey(StorageKey.SALARY_ID_KEY)
        val onlyWithSalary = getStorageByKey(StorageKey.ONLY_WITH_SALARY_KEY)
        val industry = getStorageByKey(StorageKey.INDUSTRY_ID_KEY)
        return mapOf(
            StorageKey.AREA_ID_KEY.key to area,
            StorageKey.SALARY_ID_KEY.key to salary,
            StorageKey.ONLY_WITH_SALARY_KEY.key to onlyWithSalary,
            StorageKey.INDUSTRY_ID_KEY.key to industry,
        )
    }

    private fun clearArea(): FilterStorage {
        sharedPrefs
            .edit {
                remove(StorageKey.REGION_ID_KEY.key)
                remove(StorageKey.COUNTRY_ID_KEY.key)
                remove(StorageKey.REGION_NAME_KEY.key)
                remove(StorageKey.COUNTRY_NAME_KEY.key)
                remove(StorageKey.AREA_ID_KEY.key)
            }
        return _storageState.value.copy(
            areaId = "",
            regionValue = "",
            countryValue = ""
        )
    }

    private fun clearSalary(): FilterStorage {
        sharedPrefs
            .edit {
                remove(StorageKey.SALARY_ID_KEY.key)
                remove(StorageKey.SALARY_NAME_KEY.key)
            }
        return _storageState.value.copy(
            salaryId = "",
            salaryValue = ""
        )
    }

    private fun clearIndustry(): FilterStorage {
        sharedPrefs
            .edit {
                remove(StorageKey.INDUSTRY_ID_KEY.key)
                remove(StorageKey.INDUSTRY_NAME_KEY.key)
            }
        return _storageState.value.copy(
            industryId = "",
            industryValue = ""
        )
    }

    private fun clearWithSalary(): FilterStorage {
        sharedPrefs
            .edit {
                remove(StorageKey.ONLY_WITH_SALARY_KEY.key)
            }
        return _storageState.value.copy(
            withSalary = ""
        )
    }

    private fun saveStorageByKey(key: String, value: String) {
        sharedPrefs
            .edit {
                putString(key, value)
            }
    }

    fun <T : Any> saveStorage(key: StorageKey, data: T) {
        val value = data.toString()
        saveStorageByKey(key.key, value)
        val newStorage = when (key) {
            StorageKey.REGION_NAME_KEY -> _storageState.value.copy(regionValue = value)
            StorageKey.COUNTRY_NAME_KEY -> _storageState.value.copy(countryValue = value)
            StorageKey.SALARY_ID_KEY -> _storageState.value.copy(salaryId = value)
            StorageKey.SALARY_NAME_KEY -> _storageState.value.copy(salaryValue = value)
            StorageKey.INDUSTRY_ID_KEY -> _storageState.value.copy(industryId = value)
            StorageKey.INDUSTRY_NAME_KEY -> _storageState.value.copy(industryValue = value)
            StorageKey.ONLY_WITH_SALARY_KEY -> _storageState.value.copy(withSalary = value)
            StorageKey.REGION_ID_KEY -> _storageState.value.copy(regionId = value, areaId = value)
            StorageKey.COUNTRY_ID_KEY -> {
                if (_storageState.value.regionId.isEmpty()) {
                    _storageState.value.copy(countryId = value, areaId = value)
                } else {
                    _storageState.value.copy(countryId = value)
                }
            }

            StorageKey.AREA_ID_KEY -> _storageState.value.copy(areaId = value)

        }
        _storageState.value = newStorage
    }

    fun getStorageByKey(key: StorageKey): String? {
        return sharedPrefs.getString(key.key, null)
    }

    fun getData(): Flow<Map<String, String?>> = flow {
        emit(
            getMapWithKeys()
        )
    }

    fun getDataWithNames(): Flow<FilterStorage> {
        val country = getStorageByKey(StorageKey.COUNTRY_ID_KEY)
        val region = getStorageByKey(StorageKey.REGION_ID_KEY)
        val area = if (region.isNullOrBlank()) country else region
        val salary = getStorageByKey(StorageKey.SALARY_NAME_KEY)
        val onlyWithSalary = getStorageByKey(StorageKey.ONLY_WITH_SALARY_KEY)
        val industry = getStorageByKey(StorageKey.INDUSTRY_ID_KEY)
        val industryValue = getStorageByKey(StorageKey.INDUSTRY_NAME_KEY)
        val countryValue = getStorageByKey(StorageKey.COUNTRY_NAME_KEY)
        val regionValue = getStorageByKey(StorageKey.REGION_NAME_KEY)
        val newStorage = _storageState.value.copy(
            industryValue = industryValue ?: "",
            industryId = industry ?: "",
            areaId = area ?: "",
            salaryValue = salary ?: "",
            withSalary = onlyWithSalary ?: "",
            countryValue = countryValue ?: "",
            regionValue = regionValue ?: ""
        )
        _storageState.value = newStorage
        return _storageState
    }

    fun clearByKey(key: StorageKey) {
        val newStorage = when (key) {
            StorageKey.AREA_ID_KEY, StorageKey.COUNTRY_NAME_KEY, StorageKey.REGION_NAME_KEY,
            StorageKey.COUNTRY_ID_KEY, StorageKey.REGION_ID_KEY, -> {
                clearArea()
            }

            StorageKey.SALARY_ID_KEY, StorageKey.SALARY_NAME_KEY -> {
                clearSalary()
            }

            StorageKey.INDUSTRY_ID_KEY, StorageKey.INDUSTRY_NAME_KEY -> {
                clearIndustry()
            }

            StorageKey.ONLY_WITH_SALARY_KEY -> {
                clearWithSalary()
            }
        }
        _storageState.value = newStorage
    }

    fun clearStorage() {
        sharedPrefs
            .edit {
                clear()
            }
        val newStorage = _storageState.value.copy(
            areaId = "",
            regionId = "",
            countryId = "",
            regionValue = "",
            countryValue = "",
            industryId = "",
            industryValue = "",
            salaryId = "",
            salaryValue = "",
            withSalary = "",
        )
        _storageState.value = newStorage
    }
}
