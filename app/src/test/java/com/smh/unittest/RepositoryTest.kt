package com.smh.unittest

import arrow.core.left
import arrow.core.right
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.smh.unittest.data.repository.RepositoryImpl
import com.smh.unittest.domain.repository.Repository
import com.smh.unittest.fake.ApiException
import com.smh.unittest.fake.RemoteDataSourceFake
import com.smh.unittest.fake.btc
import com.smh.unittest.fake.eth
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RepositoryTest {

    private lateinit var remoteDataSource: RemoteDataSourceFake
    private lateinit var repository: Repository

    @BeforeEach
    fun setup() {
        remoteDataSource = RemoteDataSourceFake()
        repository = RepositoryImpl(remoteDataSource)
    }

    @Test
    fun `GetCoin, return Api Exception`() = runBlocking {
        remoteDataSource.apiException = ApiException

        val actualResult = repository.getCoins()
        val expectedResult = ApiException.left()

        assertThat(actualResult.isLeft()).isTrue()
        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `GetCoin, return 2 coins`() = runBlocking {
        val actualResult = repository.getCoins()
        val expectedResult = listOf(btc, eth).right()

        assertThat(actualResult.isRight()).isTrue()
        assertThat(actualResult).isEqualTo(expectedResult)
    }
}