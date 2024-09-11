package com.smh.unittest

import arrow.core.left
import arrow.core.right
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.smh.unittest.data.CoinApi
import com.smh.unittest.data.datasource.RemoteDataSource
import com.smh.unittest.data.datasource.RemoteDataSourceImpl
import com.smh.unittest.fake.btc
import com.smh.unittest.fake.eth
import com.smh.unittest.helper.notFoundResponseBody
import com.smh.unittest.network.DataException
import com.smh.unittest.network.ERROR_MESSAGE_GENERAL
import com.smh.unittest.network.ERROR_TITLE_GENERAL
import com.smh.unittest.network.RetrofitInitializer
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import java.io.File

internal class RemoteDataSourceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var coinApi: CoinApi
    private lateinit var remoteDataSource: RemoteDataSource

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        coinApi = RetrofitInitializer.createRetrofitClient(
            url = mockWebServer.url("/").toString(),
            okHttpClient = OkHttpClient.Builder().build(),
            networkJson = RetrofitInitializer.providesNetworkJson()
        ).create(CoinApi::class.java)
        remoteDataSource = RemoteDataSourceImpl(coinApi)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `GetCoin, Response error, exception is Api 404 and NotFound`() = runBlocking {
        val responseBody = notFoundResponseBody
        val response = MockResponse()
            .setResponseCode(404)
            .setBody(responseBody)
        mockWebServer.enqueue(response)

        val actualResult = remoteDataSource.getCoins()
        val expectedResult = DataException.Api(message = "Not Found", title = ERROR_TITLE_GENERAL, errorCode = 404).left()

        // AssertK
        assertThat(actualResult.isLeft()).isTrue()
        assertThat(expectedResult).isEqualTo(actualResult)

        // Junit
        assert(actualResult.isLeft())
        assert(expectedResult == actualResult)
    }

    @Test
    fun `GetCoin, Response success, data is 2 coin`() = runBlocking {
        val responseBody = File("src/test/resources/CoinResponse.json").readText()
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(responseBody)
        mockWebServer.enqueue(response)

        val actualResult = remoteDataSource.getCoins()
        val expectedResult = listOf(btc, eth).right()

        assertThat(actualResult.isRight()).isTrue()
        assertThat(expectedResult).isEqualTo(actualResult)
    }
}

internal class RemoteDataSourceTestMockk {

    private lateinit var coinApi: CoinApi
    private lateinit var remoteDataSource: RemoteDataSource

    @BeforeEach
    fun setup() {
        coinApi = mockk()
        remoteDataSource = RemoteDataSourceImpl(coinApi)
    }

    @Test
    fun `GetCoin, Response error, exception is Api 404 and NotFound`() = runBlocking {
        coEvery { coinApi.getCoins() } returns retrofit2.Response.error(404, notFoundResponseBody.toResponseBody())

        val actualResult = remoteDataSource.getCoins()
        val expectedResult = DataException.Api(
            message = "Not Found",
            title = ERROR_TITLE_GENERAL,
            errorCode = 404
        ).left()

        assertThat(actualResult.isLeft()).isTrue()
        assertThat(expectedResult).isEqualTo(actualResult)
    }
}