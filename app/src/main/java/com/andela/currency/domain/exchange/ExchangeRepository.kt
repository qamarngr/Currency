package com.andela.currency.domain.exchange

import com.andela.currency.data.exchange.remote.dto.ExchangeRatesResponse
import com.andela.currency.data.symbols.remote.dto.SymbolsResponse
import com.andela.currency.domain.common.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {
    suspend fun getAllExchangeRates(symbols: String, base: String): Flow<BaseResult<ExchangeRatesResponse>>
}