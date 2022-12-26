package com.andela.currency.data.common.dto

import com.google.gson.annotations.SerializedName

data class ErrorDTO(@SerializedName("code") val code: Int, @SerializedName("info") val message: String)
