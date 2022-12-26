package com.andela.currency.data.symbols.remote.api

import com.andela.currency.data.symbols.remote.dto.SymbolsResponse
import retrofit2.Response
import retrofit2.http.GET

interface SymbolsApi {
    @GET("symbols")
    suspend fun getAllSymbols() : Response<SymbolsResponse>
}