package com.smh.unittest.data.repository

import arrow.core.Either
import com.smh.unittest.data.datasource.RemoteDataSource
import com.smh.unittest.domain.CoinModel
import com.smh.unittest.domain.repository.Repository
import com.smh.unittest.network.DataException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): Repository {
    override suspend fun getCoins(): Either<DataException, List<CoinModel>> {
        return remoteDataSource.getCoins()
    }
}