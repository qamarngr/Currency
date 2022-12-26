package com.andela.currency.domain.symbols

import com.andela.currency.data.symbols.remote.dto.SymbolsResponse
import com.andela.currency.domain.common.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface SymbolsRepository {
    suspend fun getAllSymbols(): Flow<BaseResult<SymbolsResponse>>
}