package ru.practicum.android.diploma.vacancy.ui.viewmodel

import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favorites.domain.interactor.FavoritesInteractor
import ru.practicum.android.diploma.favorites.domain.state.Result
import ru.practicum.android.diploma.util.NetworkUtil
import ru.practicum.android.diploma.vacancy.domain.interactor.VacancyDetailUseCase
import ru.practicum.android.diploma.vacancy.ui.state.VacancyState

class VacancyViewModel(
    private val vacancyDetailUseCase: VacancyDetailUseCase,
    savedStateHandle: SavedStateHandle,
    private val favoritesInteractor: FavoritesInteractor,
    private val networkUtil: NetworkUtil,
) : ViewModel() {

    private val _state = MutableStateFlow<VacancyState>(VacancyState.Loading)
    val state: StateFlow<VacancyState> = _state

    private val vacancyId: String = checkNotNull(savedStateHandle["vacancyId"])

    init {
        loadVacancy()
    }

    @Suppress("LabeledExpression")
    private fun loadVacancy() {
        viewModelScope.launch {
            val localVacancy = favoritesInteractor.findFavoriteVacancyForVacancyScreen(vacancyId)
            if (localVacancy is Result.Success && localVacancy.data != null) {
                _state.value = VacancyState.Success(localVacancy.data, isFavorite = true)
                return@launch
            }

            if (!networkUtil.isInternetAvailable()) {
                _state.value = VacancyState.Error
                return@launch
            }

            val vacancyDetail = vacancyDetailUseCase.getVacancyDetail(vacancyId)
            val existing = favoritesInteractor.findFavoriteVacancyForFavoriteScreen(vacancyId)
            _state.value = VacancyState.Success(
                vacancyDetail,
                isFavorite = existing is Result.Success && existing.data != null
            )
        }
    }

    fun toggleFavorite() {
        val currentState = _state.value
        if (currentState is VacancyState.Success) {
            viewModelScope.launch {
                val vacancy = currentState.vacancyDetail
                val existing = favoritesInteractor.findFavoriteVacancyForFavoriteScreen(vacancy.id)
                if (existing is Result.Success && existing.data != null) {
                    favoritesInteractor.deleteFromFavorites(vacancy)
                    _state.value = currentState.copy(isFavorite = false)
                } else {
                    favoritesInteractor.addFavoriteVacancy(vacancy)
                    _state.value = currentState.copy(isFavorite = true)
                }
            }
        }
    }

    fun shareVacancyWithMessenger(): Intent? {
        return Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, vacancyId)
            type = "text/plain"
        }, null)
    }

    fun writeWithMail(): Intent? {
        val currentState = _state.value
        if (currentState is VacancyState.Success) {
            val email = currentState.vacancyDetail.contactsEmail
            return Intent.createChooser(
                Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:".toUri()
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                },
                null
            )
        }
        return null
    }

    fun callWithPhone(): Intent? {
        val currentState = _state.value
        if (currentState is VacancyState.Success) {
            val phones = currentState.vacancyDetail.contactsPhone
            if (!phones.isNullOrEmpty()) {
                return Intent.createChooser(
                    Intent(Intent.ACTION_DIAL).apply {
                        data = "tel:${phones.first()}".toUri()
                    },
                    null
                )
            }
        }
        return null
    }
}
