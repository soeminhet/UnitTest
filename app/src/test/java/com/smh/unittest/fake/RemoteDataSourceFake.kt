package com.smh.unittest.fake

import arrow.core.Either
import com.smh.unittest.data.datasource.RemoteDataSource
import com.smh.unittest.domain.CoinModel
import com.smh.unittest.network.DataException

class RemoteDataSourceFake: RemoteDataSource {

    var apiException: DataException.Api? = null
    val coins = listOf(btc, eth)

    override suspend fun getCoins(): Either<DataException, List<CoinModel>> {
        return if (apiException != null) {
            Either.Left(apiException!!)
        } else {
            Either.Right(coins)
        }
    }
}