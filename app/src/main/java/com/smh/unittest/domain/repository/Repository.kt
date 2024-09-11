package com.smh.unittest.domain.repository

import arrow.core.Either
import com.smh.unittest.domain.CoinModel
import com.smh.unittest.network.DataException

interface Repository {
    suspend fun getCoins(): Either<DataException, List<CoinModel>>
}