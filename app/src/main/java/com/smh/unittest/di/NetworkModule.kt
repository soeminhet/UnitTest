package com.smh.unittest.di

import com.smh.unittest.data.CoinApi
import com.smh.unittest.network.RetrofitInitializer
import com.smh.unittest.network.RetrofitInitializer.createOkHttpClient
import com.smh.unittest.network.RetrofitInitializer.createRetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providesNetworkJson(): Json = RetrofitInitializer.providesNetworkJson()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return createOkHttpClient(
            interceptors = emptyList()
        )
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, networkJson: Json): Retrofit {
        return createRetrofitClient(
            "https://api.coingecko.com/api/v3/",
            okHttpClient,
            networkJson
        )
    }

    @Provides
    @Singleton
    fun provideCoinApi(
        retrofit: Retrofit
    ): CoinApi = retrofit.create(CoinApi::class.java)
}