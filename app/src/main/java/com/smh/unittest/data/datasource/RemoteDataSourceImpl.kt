package com.smh.unittest.data.datasource

import arrow.core.Either
import com.smh.unittest.data.CoinApi
import com.smh.unittest.data.mapper.toDomain
import com.smh.unittest.domain.CoinModel
import com.smh.unittest.network.DataException
import com.smh.unittest.network.handleCall
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val api: CoinApi
): RemoteDataSource{
    override suspend fun getCoins(): Either<DataException, List<CoinModel>> {
        return handleCall(
            apiCall = { api.getCoins() },
            mapper = { it.toDomain() }
        )
    }
}