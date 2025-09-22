package ru.practicum.android.diploma.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.core.domain.AppInteractor
import ru.practicum.android.diploma.core.providers.ResourceProvider
import ru.practicum.android.diploma.main.domain.interactor.SearchVacancyInteractor
import ru.practicum.android.diploma.main.domain.model.FilterRequestData
import ru.practicum.android.diploma.main.domain.model.VacancyDetailMainData
import ru.practicum.android.diploma.main.domain.model.VacancyMainData
import ru.practicum.android.diploma.main.domain.state.Resource
import ru.practicum.android.diploma.main.ui.mapper.FilterRequestMapper
import ru.practicum.android.diploma.main.ui.model.FilterRequest
import ru.practicum.android.diploma.main.ui.model.Vacancy
import ru.practicum.android.diploma.main.ui.state.SearchState
import ru.practicum.android.diploma.main.util.getFormatSalary
import ru.practicum.android.diploma.util.DebounceUtil

class SearchVacancyViewModel(

    val searchVacancyInteractor: SearchVacancyInteractor,
    val appInteractor: AppInteractor,
    val provider: ResourceProvider

) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var currentPage = 1
    private var maxPages = 0
    private var isClickAllowed = true
    private var latestRequestText: String? = null
    private val _stateSearchVacancy = MutableStateFlow<SearchState>(value = SearchState.Default)
    val stateSearchVacancy = _stateSearchVacancy.asStateFlow()

    private val _stateSearchFilter = MutableStateFlow(value = FilterRequest())
    val stateSearchFilter = _stateSearchFilter.asStateFlow()

    private val _shouldRepeatRequest = MutableStateFlow(value = false)
    val shouldRepeatRequest = _shouldRepeatRequest.asStateFlow()

    private val _vacancies = mutableListOf<Vacancy>()
    val vacancies: List<Vacancy> get() = _vacancies

    init {
        viewModelScope.launch {
            appInteractor.getAllDataWithNames().collect { data ->
                _stateSearchFilter.value = FilterRequestMapper.toFilterRequest(data)
            }
        }
    }

    val debounce = DebounceUtil(
        delayMillis = SEARCH_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope
    )

    private fun resetPages() {
        _vacancies.clear()
        currentPage = 1
        maxPages = 0
    }

    private fun searchVacancy(expression: String, isLazyLoad: Boolean = false) {
        if (expression.isNotEmpty()) {
            if (!isLazyLoad) {
                renderSearchState(
                    state = SearchState.Loading
                )
            }
            val currentContent = _stateSearchVacancy.value as? SearchState.Content
            currentContent?.let {
                renderSearchState(
                    state = it.copy(
                        isLoadingNextPage = true
                    )
                )
            }

            debounce.invoke {
                val filter: FilterRequestData = FilterRequestMapper.toFilterRequestData(_stateSearchFilter.value)
                searchVacancyInteractor.searchVacancy(expression, currentPage, filter)
                    .collect { vacancy ->
                        searchState(vacancy = vacancy)
                    }
            }
        } else {
            debounce.cancel()
        }
    }

    private fun map(items: List<VacancyDetailMainData>): List<Vacancy> {
        return items.map {
            Vacancy(
                name = it.name,
                id = it.id,
                logoUrl = it.employer.logo ?: "",
                industry = it.employer.name ?: "",
                salary = it.salary.getFormatSalary(provider = provider),
                city = it.city
            )
        }
    }

    fun showMore() {
        if (currentPage <= maxPages) {
            latestRequestText?.let {
                if (it.isNotEmpty()) {
                    searchRequestText(expression = it, isLazyLoad = true)
                }
            }
        }
    }

    fun searchRequestText(expression: String, isLazyLoad: Boolean = false, shouldRepeat: Boolean = false) {
        if (!shouldRepeat) {
            if (latestRequestText == expression) {
                if (!isLazyLoad) {
                    return
                }
            } else if (latestRequestText?.isNotEmpty() == true && !isLazyLoad) {
                resetPages()
            }
            if (expression.isEmpty()) {
                resetPages()
            }
        }
        latestRequestText = expression
        searchVacancy(
            expression = expression,
            isLazyLoad = isLazyLoad
        )
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            debounce.invoke {
                isClickAllowed = true
            }
        }
        return current
    }

    fun renderDefaultState() {
        debounce.cancel()
        resetPages()
        latestRequestText = ""
        renderSearchState(SearchState.Default)
    }

    fun searchState(vacancy: Resource<VacancyMainData>) {
        when (vacancy) {
            is Resource.Success -> {
                val vacancy = vacancy.data
                if (maxPages == 0) {
                    maxPages = vacancy.pages
                }
                if (vacancy.items.isNotEmpty()) {
                    _vacancies.addAll(map(vacancy.items))
                    renderSearchState(
                        state = SearchState.Content(
                            vacancy = vacancies,
                            countVacancy = vacancy.found,
                            isLoadingNextPage = false
                        )
                    )
                    currentPage++
                } else {
                    renderSearchState(
                        state = SearchState.Empty
                    )
                }
            }

            is Resource.Error -> {
                if (vacancy.isLazyError) {
                    val lastState = stateSearchVacancy.value
                    if (lastState is SearchState.Content) {
                        lastState.countVacancy
                        renderSearchState(
                            state = SearchState.Content(
                                vacancy = vacancies,
                                countVacancy = lastState.countVacancy,
                                isLoadingNextPage = false,
                                isLazyError = true
                            )
                        )
                    }
                } else {
                    renderSearchState(
                        state = SearchState.Error
                    )
                }
            }
        }
    }

    fun renderSearchState(state: SearchState) {
        _stateSearchVacancy.value = state
    }

    fun setShouldRepeatRequest(shouldRepeat: Boolean) {
        _shouldRepeatRequest.value = shouldRepeat
    }

    fun repeatRequest() {
        resetPages()
        searchRequestText(expression = latestRequestText ?: "", shouldRepeat = true)
    }
}
