package ru.practicum.android.diploma.core.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.core.data.dto.FilterAreasResponse
import ru.practicum.android.diploma.core.data.dto.FilterIndustryResponse
import ru.practicum.android.diploma.core.data.dto.Response
import ru.practicum.android.diploma.core.data.dto.VacancyDetailResponse
import ru.practicum.android.diploma.core.data.dto.VacancyRequest
import ru.practicum.android.diploma.core.data.dto.VacancyResponse
import ru.practicum.android.diploma.core.data.utils.ResponseCode
import ru.practicum.android.diploma.core.data.utils.toQueryMap
import java.io.IOException

/**
 * Класс, отвечающий за выполнение сетевых запросов к API вакансий.
 *
 * Все методы обрабатывают возможные ошибки сети и HTTP-ответов и возвращают объект [Response] с кодом результата.
 *
 * @property apiService [VacancyApiService] интерфейс для вызова сетевых методов API.
 */
class VacancyNetworkClient(
    private val apiService: VacancyApiService
) {

    /**
     * Универсальный метод для безопасного выполнения сетевого запроса.
     *
     * Оборачивает вызов API в блок try/catch и обрабатывает исключения:
     *  - [IOException] — проблемы с сетью ([ResponseCode.IO_ERROR])
     *  - [HttpException] — ошибки HTTP (код из ответа сервера)
     *
     * @param apiCall Функция прерывания, выполняющая конкретный сетевой запрос.
     * @return [T] объект ответа, наследник [Response], с заполненным `resultCode`.
     */
    private suspend fun <T : Response> safeApiCall(apiCall: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            try {
                apiCall().apply { resultCode = ResponseCode.SUCCESS }
            } catch (e: IOException) {
                Log.e("VacancyNetworkClient", "Network error", e)
                Response().apply { resultCode = ResponseCode.IO_ERROR } as T
            } catch (e: HttpException) {
                Log.e("VacancyNetworkClient", "HTTP error ${e.code()}", e)
                Response().apply { resultCode = e.code() } as T
            }
        }
    }

    /**
     * Получает список фильтров районов через API.
     *
     * @return [Response] объект, содержащий результат запроса и код ответа `resultCode`.
     */
    suspend fun getFilterAreas(): FilterAreasResponse =
        safeApiCall {
            FilterAreasResponse(
                areas = apiService.getFilterAreas()
            )
        }

    /**
     * Получает список фильтров отраслей через API.
     *
     * @return [Response] объект, содержащий результат запроса и код ответа `resultCode`.
     */
    suspend fun getFilterIndustries(): FilterIndustryResponse =
        safeApiCall {
            FilterIndustryResponse(
                industries = apiService.getFilterIndustries()
            )
        }

    /**
     * Получает список вакансий с фильтрацией по параметрам запроса.
     *
     * @param request [VacancyRequest] объект с параметрами фильтрации.
     * @return [Response] объект, содержащий список вакансий и код ответа `resultCode`.
     */
    suspend fun getVacancies(request: VacancyRequest): VacancyResponse =
        safeApiCall {
            VacancyResponse(
                vacancy = apiService.getVacancies(request.toQueryMap())
            )
        }

    /**
     * Получает подробную информацию по конкретной вакансии по ID.
     *
     * @param id ID вакансии.
     * @return [Response] объект, содержащий детальную информацию о вакансии и код ответа `resultCode`.
     */
    suspend fun getVacancy(id: String): VacancyDetailResponse =
        safeApiCall {
            VacancyDetailResponse(
                vacancyDetailDto = apiService.getVacancy(id)
            )
        }
}
