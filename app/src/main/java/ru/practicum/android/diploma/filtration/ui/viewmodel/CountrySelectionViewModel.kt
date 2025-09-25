package ru.practicum.android.diploma.filtration.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import ru.practicum.android.diploma.core.domain.AppInteractor
import ru.practicum.android.diploma.core.domain.repository.StorageKey
import ru.practicum.android.diploma.filtration.domain.interactor.GetAreasUseCase
import ru.practicum.android.diploma.filtration.domain.model.Country
import ru.practicum.android.diploma.filtration.domain.state.Result
import ru.practicum.android.diploma.filtration.ui.state.CountrySelectionScreenState
import ru.practicum.android.diploma.util.NetworkUtil

class CountrySelectionViewModel(
    private val getAreasUseCase: GetAreasUseCase,
    private val appInteractor: AppInteractor,
    private val networkUtil: NetworkUtil,
) : ViewModel() {
    private val _screenState = MutableStateFlow<CountrySelectionScreenState>(CountrySelectionScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        if (!networkUtil.isInternetAvailable()) {
            _screenState.value = CountrySelectionScreenState.Error
            return
        }
        viewModelScope.launch {
            try {
                when (val result = getAreasUseCase()) {
                    is Result.Success -> {
                        val countries = result.data.first
                        _screenState.value = CountrySelectionScreenState.Data(countries)
                    }

                    is Result.Error -> {
                        _screenState.value = CountrySelectionScreenState.Error
                    }
                }
            } catch (e: IOException) {
                Log.e("RegionsViewModel", "Error loading areas", e)
                _screenState.value = CountrySelectionScreenState.Error
            }
        }
    }

    fun onCountryClicked(
        country: Country,
        navController: NavController,
    ) {
        appInteractor.saveData(StorageKey.COUNTRY_ID_KEY, country.id)

        appInteractor.saveData(StorageKey.COUNTRY_NAME_KEY, country.name)

        appInteractor.saveData(StorageKey.REGION_NAME_KEY, "")
        navController.popBackStack()
    }
}
