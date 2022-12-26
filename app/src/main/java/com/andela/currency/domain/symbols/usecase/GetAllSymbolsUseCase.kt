package com.andela.currency.domain.symbols.usecase

import com.andela.currency.data.symbols.remote.dto.SymbolsResponse
import com.andela.currency.domain.common.base.BaseResult
import com.andela.currency.domain.symbols.SymbolsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSymbolsUseCase @Inject constructor(private val productRepository: SymbolsRepository) {
    suspend fun invoke() : Flow<BaseResult<SymbolsResponse>> {
        return productRepository.getAllSymbols()
    }
}