package com.smh.unittest.data

import com.smh.unittest.data.dto.CoinResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinApi {
    @GET("coins/markets")
    suspend fun getCoins(
        @Query("vs_currency") currency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("price_change_percentage") priceChangePercentage: String = "24h",
        @Query("sparkline") sparkline: Boolean = true,
        @Query("per_page") perPage: Int = 250,
        @Query("page") page: Int = 1,
    ): Response<List<CoinResponse>>
}