package ru.practicum.android.diploma.favorites.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.model.VacancyFavorites
import ru.practicum.android.diploma.favorites.domain.state.Result
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail

interface FavoritesInteractor {

    fun getFavoritesVacancy(): Flow<Result<List<VacancyFavorites?>>>

    suspend fun addFavoriteVacancy(vacancy: VacancyDetail): Result<Long>

    suspend fun findFavoriteVacancyForFavoriteScreen(id: String): Result<VacancyFavorites?>

    suspend fun deleteFromFavorites(vacancy: VacancyDetail): Result<Unit>

    suspend fun findFavoriteVacancyForVacancyScreen(id: String): Result<VacancyDetail?>
}
