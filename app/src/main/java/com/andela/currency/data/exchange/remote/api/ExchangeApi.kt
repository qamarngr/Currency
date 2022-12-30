package com.andela.currency.data.exchange.remote.api

import com.andela.currency.data.exchange.remote.dto.ExchangeRatesResponse
import com.andela.currency.data.symbols.remote.dto.SymbolsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryName

interface ExchangeApi {
    @GET("latest")
    suspend fun getAllExchangeRates(
        @Query("symbol") symbol: String,
        @Query("base") baseSymbol: String
    ): Response<ExchangeRatesResponse>
}