package com.andela.currency.data.common.utils

import com.andela.currency.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor  : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val apiKey = BuildConfig.API_KEY
        val newRequest = chain.request().newBuilder()
            .addHeader("apikey", apiKey)
            .build()
        return chain.proceed(newRequest)
    }
}