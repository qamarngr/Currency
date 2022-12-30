package com.andela.currency.data.exchange.remote.dto

import com.andela.currency.data.common.dto.BaseResponse
import java.util.*

data class ExchangeRatesResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
) : BaseResponse()
