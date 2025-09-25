package ru.practicum.android.diploma.filtration.data.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.core.data.network.VacancyNetworkClient
import ru.practicum.android.diploma.core.data.utils.ResponseCode
import ru.practicum.android.diploma.filtration.data.mapper.IndustriesMapper
import ru.practicum.android.diploma.filtration.domain.model.Industries
import ru.practicum.android.diploma.filtration.domain.model.StorageResultCode
import ru.practicum.android.diploma.filtration.domain.repository.IndustriesRepository
import ru.practicum.android.diploma.filtration.domain.state.Resource
import ru.practicum.android.diploma.util.NetworkUtil

class IndustriesRepositoryImpl(
    val networkClient: VacancyNetworkClient,
    val networkUtil: NetworkUtil,
    val context: Context
) : IndustriesRepository {

    private fun initError(storageCode: StorageResultCode): Resource.Error<Industries> {
        return Resource.Error(
            Industries(
                items = emptyList(),
                resultCode = storageCode,
            )
        )
    }

    override suspend fun getIndustries(): Flow<Resource<Industries>> {
        return flow {
            if (!networkUtil.isInternetAvailable()) {
                emit(
                    initError(storageCode = StorageResultCode.CLIENT_ERROR)
                )
            } else {
                val response = networkClient.getFilterIndustries()
                when (response.resultCode) {
                    ResponseCode.SUCCESS -> {
                        emit(
                            Resource.Success(
                                IndustriesMapper.toIndustries(
                                    data = networkClient.getFilterIndustries(),
                                )
                            )
                        )
                    }

                    ResponseCode.IO_ERROR -> emit(
                        initError(storageCode = StorageResultCode.SERVER_ERROR)
                    )

                    else -> emit(
                        initError(storageCode = StorageResultCode.SERVER_ERROR)
                    )
                }
            }
        }
    }
}
