package ru.practicum.android.diploma.filtration.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import ru.practicum.android.diploma.core.domain.AppInteractor
import ru.practicum.android.diploma.core.domain.repository.StorageKey
import ru.practicum.android.diploma.filtration.domain.interactor.GetAreasUseCase
import ru.practicum.android.diploma.filtration.domain.model.Country
import ru.practicum.android.diploma.filtration.domain.model.Region
import ru.practicum.android.diploma.filtration.domain.state.Result
import ru.practicum.android.diploma.filtration.ui.state.RegionSelectionScreenState
import ru.practicum.android.diploma.util.DebounceUtil
import ru.practicum.android.diploma.util.NetworkUtil

class RegionSelectionViewModel(
    private val getAreasUseCase: GetAreasUseCase,
    private val appInteractor: AppInteractor,
    private val networkUtil: NetworkUtil,
) : ViewModel() {
    private val _screenState =
        MutableStateFlow<RegionSelectionScreenState>(RegionSelectionScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    private var loadJob: Job? = null
    private var allRegions: List<Region> = emptyList()
    private var countries: List<Country> = emptyList()

    companion object {
        const val DEBOUNCE_DELAY = 1000L
    }

    private val searchDebounce = DebounceUtil(DEBOUNCE_DELAY, viewModelScope)

    fun loadData(countryId: Int? = null) {
        loadJob?.cancel()
        if (!networkUtil.isInternetAvailable()) {
            _screenState.value = RegionSelectionScreenState.Error
            return
        }
        loadJob = viewModelScope.launch {
            try {
                when (val result = getAreasUseCase()) {
                    is Result.Success -> {
                        countries = result.data.first
                        val regionsMap = result.data.second
                        val fullList = regionsMap.values.flatten()

                        allRegions = if (countryId != null) {
                            fullList.filter { it.countryId == countryId }
                        } else {
                            fullList
                        }

                        _screenState.value = if (allRegions.isEmpty()) {
                            RegionSelectionScreenState.EmptyResult
                        } else {
                            RegionSelectionScreenState.Data(
                                allRegions = allRegions,
                                filteredRegions = allRegions
                            )
                        }
                    }
                    is Result.Error -> {
                        _screenState.value = RegionSelectionScreenState.Error
                    }
                }
            } catch (e: IOException) {
                Log.e("RegionsViewModel", "Error loading areas", e)
                _screenState.value = RegionSelectionScreenState.Error
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        if (!networkUtil.isInternetAvailable() && allRegions.isEmpty()) {
            _screenState.value = RegionSelectionScreenState.Error
            return
        }
        if (query.isBlank()) {
            _screenState.value = RegionSelectionScreenState.Data(
                allRegions = allRegions,
                filteredRegions = allRegions
            )
        }
        searchDebounce.invoke {
            filterRegions(query)
        }
    }

    private fun filterRegions(query: String) {
        if (query.isBlank()) {
            _screenState.value = RegionSelectionScreenState.Data(
                allRegions = allRegions,
                filteredRegions = allRegions
            )
            return
        }

        val filtered = allRegions.filter { it.name.contains(query, ignoreCase = true) }

        _screenState.value = if (filtered.isEmpty()) {
            RegionSelectionScreenState.EmptyResult
        } else {
            RegionSelectionScreenState.Data(
                allRegions = allRegions,
                filteredRegions = filtered
            )
        }
    }

    fun onRegionClicked(region: Region, navController: NavController) {
        appInteractor.saveData(StorageKey.REGION_ID_KEY, region.id)
        appInteractor.saveData(StorageKey.REGION_NAME_KEY, region.name)

        val country = countries.find { it.id == region.countryId }
        if (country != null) {
            appInteractor.saveData(StorageKey.COUNTRY_ID_KEY, country.id)
            appInteractor.saveData(StorageKey.COUNTRY_NAME_KEY, country.name)
        }
        navController.popBackStack()
    }
}
