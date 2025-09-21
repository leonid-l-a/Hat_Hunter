package ru.practicum.android.diploma.favorites.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.SQLiteException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favorites.domain.interactor.FavoritesInteractor
import ru.practicum.android.diploma.favorites.ui.state.FavoriteState
import ru.practicum.android.diploma.util.DebounceUtil

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private var isClickAllowed = true

    private val _stateFavoritesVacancy = MutableStateFlow<FavoriteState>(FavoriteState.Empty)
    val stateFavoritesVacancy = _stateFavoritesVacancy.asStateFlow()

    val debounce = DebounceUtil(
        delayMillis = LOADING_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope
    )

    init {
        viewModelScope.launch {
            favoritesInteractor.getFavoritesVacancy()
                .catch { e ->
                    if (e is SQLiteException) {
                        _stateFavoritesVacancy.value = FavoriteState.Error(e.message)
                    } else {
                        throw e
                    }
                }
                .collect { favorites ->
                    val nonNullFavorites = favorites.filterNotNull()
                    _stateFavoritesVacancy.value = when {
                        nonNullFavorites.isEmpty() -> FavoriteState.Empty
                        else -> FavoriteState.Content(nonNullFavorites)
                    }
                }
        }
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

    companion object {
        private const val LOADING_DEBOUNCE_DELAY = 2000L
    }
}
