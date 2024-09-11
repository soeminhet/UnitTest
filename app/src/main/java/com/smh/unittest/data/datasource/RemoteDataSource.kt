package com.smh.unittest.data.datasource

import arrow.core.Either
import com.smh.unittest.domain.CoinModel
import com.smh.unittest.network.DataException

interface RemoteDataSource {
    suspend fun getCoins(): Either<DataException, List<CoinModel>>
}