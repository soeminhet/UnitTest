package com.smh.unittest

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.smh.unittest.data.datasource.RemoteDataSource
import com.smh.unittest.data.repository.RepositoryImpl
import com.smh.unittest.domain.repository.Repository
import com.smh.unittest.fake.ApiException
import com.smh.unittest.fake.RemoteDataSourceFake
import com.smh.unittest.fake.btc
import com.smh.unittest.fake.eth
import com.smh.unittest.helper.TestCoroutineExtension
import com.smh.unittest.ui.presentation.HomeUiEvent
import com.smh.unittest.ui.presentation.HomeViewModel
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestCoroutineExtension::class)
internal class ViewModelTest {

    private lateinit var remoteDataSource: RemoteDataSourceFake
    private lateinit var repository: Repository
    private lateinit var viewModel: HomeViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
//        val testDispatcher = StandardTestDispatcher()
//        Dispatchers.setMain(testDispatcher)

        remoteDataSource = RemoteDataSourceFake()
        repository = RepositoryImpl(remoteDataSource)
        viewModel = HomeViewModel(repository)
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    @AfterEach
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }

    @Test
    fun testUiState_success() = runTest {
        viewModel.uiState.test {
            val emission1 = awaitItem()
            assertThat(emission1.loading).isFalse()

            viewModel.fetchCoins()
            val emission2 = awaitItem()
            assertThat(emission2.loading).isTrue()

            val emission3 = awaitItem()
            assertThat(emission3.loading).isFalse()
            assertThat(emission3.coins.size).isEqualTo(0)

            val emission4 = awaitItem()
            assertThat(emission4.coins.size).isEqualTo(2)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testUiState_error() = runTest() {
        viewModel.uiState.test {
            val emission1 = awaitItem()
            assertThat(emission1.loading).isFalse()

            remoteDataSource.apiException = ApiException
            viewModel.fetchCoins()
            val emission2 = awaitItem()
            assertThat(emission2.loading).isTrue()
            assertThat(emission2.coins.size).isEqualTo(0)

            val emission3 = awaitItem()
            assertThat(emission3.loading).isFalse()
            assertThat(emission3.coins.size).isEqualTo(0)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testUiEvent() = runTest {
        viewModel.uiEvent.test {
            remoteDataSource.apiException = ApiException
            viewModel.fetchCoins()

            val emission = awaitItem()
            assertThat(emission).isEqualTo(HomeUiEvent.Error("Error"))

            // cancelAndIgnoreRemainingEvents()
        }
    }
}

@ExtendWith(TestCoroutineExtension::class)
internal class ViewModelTestMockk {
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var repository: Repository
    private lateinit var viewModel: HomeViewModel

    @BeforeEach
    fun setup() {
        remoteDataSource = mockk()
        repository = RepositoryImpl(remoteDataSource)
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun testUiState_success() = runTest {
        viewModel.uiState.test {
            val emission1 = awaitItem()
            assertThat(emission1.loading).isFalse()

            coEvery { remoteDataSource.getCoins() } returns listOf(btc, eth).right()
            viewModel.fetchCoins()
            val emission2 = awaitItem()
            assertThat(emission2.loading).isTrue()

            val emission3 = awaitItem()
            assertThat(emission3.loading).isFalse()
            assertThat(emission3.coins.size).isEqualTo(0)

            val emission4 = awaitItem()
            assertThat(emission4.coins.size).isEqualTo(2)

            cancelAndIgnoreRemainingEvents()

            coVerify(exactly = 1) {
                repository.getCoins()
                remoteDataSource.getCoins()
            }
        }
    }

    @Test
    fun testUiState_error() = runTest {
        viewModel.uiState.test {
            val emission1 = awaitItem()
            assertThat(emission1.loading).isFalse()

            coEvery { remoteDataSource.getCoins() } returns ApiException.left()
            viewModel.fetchCoins()
            val emission2 = awaitItem()
            assertThat(emission2.loading).isTrue()
            assertThat(emission2.coins.size).isEqualTo(0)

            val emission3 = awaitItem()
            assertThat(emission3.loading).isFalse()
            assertThat(emission3.coins.size).isEqualTo(0)

            cancelAndIgnoreRemainingEvents()

            coVerify(atLeast = 1) {
                repository.getCoins()
                remoteDataSource.getCoins()
            }
        }
    }

    @Test
    fun testUiEvent() = runTest {
        viewModel.uiEvent.test {
            coEvery { remoteDataSource.getCoins() } returns ApiException.left()
            viewModel.fetchCoins()

            val emission = awaitItem()
            assertThat(emission).isEqualTo(HomeUiEvent.Error("Error"))

            cancelAndIgnoreRemainingEvents()

            coVerify(exactly = 1) {
                repository.getCoins()
                remoteDataSource.getCoins()
            }
        }
    }
}