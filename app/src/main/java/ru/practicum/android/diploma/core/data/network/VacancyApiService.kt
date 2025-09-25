package ru.practicum.android.diploma.core.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.core.data.dto.FilterAreas
import ru.practicum.android.diploma.core.data.dto.FilterAreasResponse
import ru.practicum.android.diploma.core.data.dto.FilterIndustryDetail
import ru.practicum.android.diploma.core.data.dto.FilterIndustryResponse
import ru.practicum.android.diploma.core.data.dto.Vacancy
import ru.practicum.android.diploma.core.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.core.data.dto.VacancyDetailResponse
import ru.practicum.android.diploma.core.data.dto.VacancyResponse

/**
 * Интерфейс API для работы с вакансиями.
 *
 * Содержит методы для получения списка вакансий, фильтров по районам и отраслям,
 * а также получения детальной информации о конкретной вакансии.
 *
 * Все методы являются suspend-функциями и должны вызываться из корутины.
 */
interface VacancyApiService {

    /**
     * Получает список доступных фильтров районов.
     *
     * Каждый элемент списка представляет отдельный район, который можно использовать для фильтрации вакансий.
     *
     * @return [FilterAreasResponse] объект, содержащий список районов [FilterAreasResponse].
     *
     * Пример использования:
     * ```
     * val response = apiService.getFilterAreas()
     * response.areas.forEach { println(it.name) }
     * ```
     */
    @GET("areas")
    suspend fun getFilterAreas(): List<FilterAreas>

    /**
     * Получает список доступных фильтров отраслей.
     *
     * Каждый элемент списка представляет отдельную отрасль, которую можно использовать для фильтрации вакансий.
     *
     * @return [FilterIndustryResponse] объект, содержащий список отраслей [FilterIndustryDetail].
     *
     * Пример использования:
     * ```
     * val response = apiService.getFilterIndustries()
     * response.industries.forEach { println(it.name) }
     * ```
     */
    @GET("industries")
    suspend fun getFilterIndustries(): List<FilterIndustryDetail>

    /**
     * Получает список вакансий с фильтрацией по переданным параметрам.
     *
     * @param params Map с параметрами запроса. Ключи должны соответствовать именам query-параметров API:
     *  - "text" (String) — поисковая строка,
     *  - "area" (Int) — ID региона (опционально),
     *  - "industry" (Int) — ID отрасли (опционально),
     *  - "salary" (Int) — минимальная зарплата (опционально),
     *  - "page" (Int) — номер страницы (опционально),
     *  - "only_with_salary" (Boolean) — фильтр только с зарплатой (по умолчанию false)
     *
     * @return [VacancyResponse] объект с результатами поиска вакансий.
     */
    @GET("vacancies")
    suspend fun getVacancies(
        @QueryMap params: @JvmSuppressWildcards Map<String, Any?>
    ): Vacancy

    /**
     * Получает детальную информацию о вакансии по её ID.
     *
     * @param id Идентификатор вакансии, по которому будет произведён запрос.
     *
     * @return [VacancyDetailResponse] объект с полной информацией о вакансии.
     *
     * Пример использования:
     * ```
     * val vacancy = apiService.getVacancy(123)
     * println(vacancy.title)
     * ```
     */
    @GET("vacancies/{id}")
    suspend fun getVacancy(
        @Path("id") id: String
    ): VacancyDetailDto
}
