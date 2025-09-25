package ru.practicum.android.diploma.filtration.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.AppInteractor
import ru.practicum.android.diploma.core.domain.repository.StorageKey

class WorkPlaceViewModel(
    private val appInteractor: AppInteractor,
) : ViewModel() {

    private val _countryName = MutableStateFlow("")
    val countryName = _countryName.asStateFlow()

    private val _regionName = MutableStateFlow("")
    val regionName = _regionName.asStateFlow()

    private val _countryId = MutableStateFlow<String?>(null)
    val countryId = _countryId.asStateFlow()

    private val _regionId = MutableStateFlow<String?>(null)
    val regionId = _regionId.asStateFlow()

    init {
        viewModelScope.launch {
            appInteractor.getAllDataWithNames().collect {
                val country = appInteractor.getData(StorageKey.COUNTRY_NAME_KEY) ?: ""
                val region = appInteractor.getData(StorageKey.REGION_NAME_KEY) ?: ""
                val countryId = appInteractor.getData(StorageKey.COUNTRY_ID_KEY) ?: ""
                val regionId = appInteractor.getData(StorageKey.REGION_ID_KEY) ?: ""
                _countryName.value = country
                _regionName.value = region
                _countryId.value = countryId
                _regionId.value = regionId
            }
        }
    }

    fun clearCountry() {
        appInteractor.clearByKey(StorageKey.COUNTRY_NAME_KEY)
        if (_regionName.value.isBlank()) {
            appInteractor.clearByKey(StorageKey.AREA_ID_KEY)
        }
        appInteractor.clearByKey(StorageKey.REGION_NAME_KEY)
        appInteractor.clearByKey(StorageKey.COUNTRY_ID_KEY)
    }

    fun clearRegion() {
        appInteractor.clearByKey(StorageKey.REGION_NAME_KEY)
        if (_countryName.value.isBlank()) {
            appInteractor.clearByKey(StorageKey.AREA_ID_KEY)
        }
        appInteractor.clearByKey(StorageKey.REGION_ID_KEY)
    }
}
