package ru.practicum.android.diploma.favorites.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.model.VacancyFavorites
import ru.practicum.android.diploma.favorites.domain.repository.FavoritesRepository
import ru.practicum.android.diploma.favorites.domain.state.Result
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
) : FavoritesInteractor {
    override fun getFavoritesVacancy(): Flow<Result<List<VacancyFavorites?>>> {
        return favoritesRepository.favoritesVacancy()
    }

    override suspend fun addFavoriteVacancy(vacancy: VacancyDetail): Result<Long> {
        return favoritesRepository.addFavorite(vacancy)
    }

    override suspend fun findFavoriteVacancyForFavoriteScreen(id: String): Result<VacancyFavorites?> {
        return favoritesRepository.findFavorite(id)
    }

    override suspend fun deleteFromFavorites(vacancy: VacancyDetail): Result<Unit> {
        return favoritesRepository.deleteFromFavorites(vacancy)
    }

    override suspend fun findFavoriteVacancyForVacancyScreen(id: String): Result<VacancyDetail?> {
        return favoritesRepository.findFavoriteForDetail(id)
    }
}
