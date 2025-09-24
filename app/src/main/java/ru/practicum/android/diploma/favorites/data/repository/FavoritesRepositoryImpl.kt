package ru.practicum.android.diploma.favorites.data.repository

import android.database.sqlite.SQLiteException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.favorites.data.db.FavoritesEntity
import ru.practicum.android.diploma.favorites.data.db.dao.FavoritesDao
import ru.practicum.android.diploma.favorites.data.mapper.VacancyDbMapper
import ru.practicum.android.diploma.favorites.data.mapper.toDetail
import ru.practicum.android.diploma.favorites.domain.model.VacancyFavorites
import ru.practicum.android.diploma.favorites.domain.repository.FavoritesRepository
import ru.practicum.android.diploma.favorites.domain.state.Result
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail
import kotlin.coroutines.cancellation.CancellationException

class FavoritesRepositoryImpl(
    private val favoritesDao: FavoritesDao
) : FavoritesRepository {
    override fun favoritesVacancy(): Flow<Result<List<VacancyFavorites?>>> {
        return favoritesDao.getAllFavoritesVacancy()
            .map<List<FavoritesEntity>, Result<List<VacancyFavorites?>>> { favoritesVacancy ->
                val mapped = favoritesVacancy.map { VacancyDbMapper.favoritesEntityToVacancyFavorites(it) }
                Result.Success(mapped)
            }
            .catch { e ->
                if (e is CancellationException) throw e
                emit(Result.Error(e.message))
            }
    }

    override suspend fun addFavorite(vacancy: VacancyDetail): Result<Long> {
        return try {
            val id = favoritesDao.insertFavoriteVacancy(
                VacancyDbMapper.vacancyDetailToFavoritesEntity(vacancy)
            )
            Result.Success(id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: SQLiteException) {
            Result.Error(e.message)
        }
    }

    override suspend fun findFavorite(id: String): Result<VacancyFavorites?> {
        return try {
            val entity = favoritesDao.findById(id)
            Result.Success(VacancyDbMapper.favoritesEntityToVacancyFavorites(entity))
        } catch (e: CancellationException) {
            throw e
        } catch (e: SQLiteException) {
            Result.Error(e.message)
        }
    }

    override suspend fun deleteFromFavorites(vacancy: VacancyDetail): Result<Unit> {
        return try {
            favoritesDao.deleteById(vacancy.id)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: SQLiteException) {
            Result.Error(e.message)
        }
    }

    override suspend fun findFavoriteForDetail(id: String): Result<VacancyDetail?> {
        return try {
            val entity = favoritesDao.findById(id)
            val vacancy = VacancyDbMapper.favoritesEntityToVacancyFavorites(entity)?.toDetail()
            Result.Success(vacancy)
        } catch (e: CancellationException) {
            throw e
        } catch (e: SQLiteException) {
            Result.Error(e.message)
        }
    }
}
