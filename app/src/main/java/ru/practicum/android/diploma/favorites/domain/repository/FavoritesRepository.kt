package ru.practicum.android.diploma.favorites.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.model.VacancyFavorites
import ru.practicum.android.diploma.favorites.domain.state.Result
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail

interface FavoritesRepository {

    fun favoritesVacancy(): Flow<Result<List<VacancyFavorites?>>>

    suspend fun addFavorite(vacancy: VacancyDetail): Result<Long>

    suspend fun findFavorite(id: String): Result<VacancyFavorites?>

    suspend fun deleteFromFavorites(vacancy: VacancyDetail): Result<Unit>

    suspend fun findFavoriteForDetail(id: String): Result<VacancyDetail?>
}
