package com.andela.currency.data.exchange.repository

import com.andela.currency.data.common.dto.BaseResponse
import com.andela.currency.data.exchange.remote.api.ExchangeApi
import com.andela.currency.data.exchange.remote.dto.ExchangeRatesResponse
import com.andela.currency.domain.common.base.BaseResult
import com.andela.currency.domain.exchange.ExchangeRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(private val exchangeApi: ExchangeApi) :
    ExchangeRepository {
    override suspend fun getAllExchangeRates(
        symbols: String,
        baseCurrency: String
    ): Flow<BaseResult<ExchangeRatesResponse>> {
        return flow {
            val response = exchangeApi.getAllExchangeRates(symbols, baseCurrency)
            if (response.isSuccessful) {
                emit(BaseResult.Success(response.body()!!))
            } else {
                val type = object : TypeToken<BaseResponse>() {}.type
                val err = Gson().fromJson<BaseResponse>(response.errorBody()!!.charStream(), type)!!
                emit(BaseResult.Error(ExchangeRatesResponse("", "", mapOf()).apply {
                    error = err.error
                    success = false
                }))
            }
        }
    }

}