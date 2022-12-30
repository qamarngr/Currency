package com.andela.currency.domain.exchange.usecase

import com.andela.currency.data.exchange.remote.dto.ExchangeRatesResponse
import com.andela.currency.data.symbols.remote.dto.SymbolsResponse
import com.andela.currency.domain.common.base.BaseResult
import com.andela.currency.domain.exchange.ExchangeRepository
import com.andela.currency.domain.symbols.SymbolsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllExchangeRatesUseCase @Inject constructor(private val exchangeRepository: ExchangeRepository) {
    suspend fun invoke(base: String) : Flow<BaseResult<ExchangeRatesResponse>> {
        return exchangeRepository.getAllExchangeRates("symbols", base)
    }
}