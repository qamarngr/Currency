package com.andela.currency.data.symbols.repository

import android.util.Log
import com.andela.currency.data.common.dto.BaseResponse
import com.andela.currency.data.symbols.remote.api.SymbolsApi
import com.andela.currency.data.symbols.remote.dto.SymbolsResponse
import com.andela.currency.domain.common.base.BaseResult
import com.andela.currency.domain.symbols.SymbolsRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SymbolsRepositoryImpl @Inject constructor(private val symbolsApi: SymbolsApi) : SymbolsRepository {
    override suspend fun getAllSymbols(): Flow<BaseResult<SymbolsResponse>> {
        return flow {
            val response = symbolsApi.getAllSymbols()
            if (response.isSuccessful){
                Log.d("Symbol", "Imple "+ response.raw().body)
                val body = response.body()!!

                emit(BaseResult.Success(response.body()!!))
            } else {
                val type = object : TypeToken<BaseResponse>(){}.type
                val err = Gson().fromJson<BaseResponse>(response.errorBody()!!.charStream(), type)!!
                emit(BaseResult.Error(SymbolsResponse(mapOf()).apply {
                    error = err.error
                    success = false
                }))
            }
        }
    }

}