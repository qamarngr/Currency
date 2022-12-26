package com.andela.currency.data.common.dto

import com.google.gson.annotations.SerializedName

open class BaseResponse {
    @SerializedName("success")
    var success: Boolean = false

    @SerializedName("error")
    var error: ErrorDTO? = null


    fun isSuccess(): Boolean {
        return success && error == null
    }
}