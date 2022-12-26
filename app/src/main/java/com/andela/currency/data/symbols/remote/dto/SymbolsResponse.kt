package com.andela.currency.data.symbols.remote.dto

import com.andela.currency.data.common.dto.BaseResponse
import com.google.gson.annotations.SerializedName

data class SymbolsResponse(@SerializedName("symbols") val symbols: Map<String, String>) :
    BaseResponse()

