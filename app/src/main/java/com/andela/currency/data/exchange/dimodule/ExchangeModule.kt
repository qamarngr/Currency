package com.andela.currency.data.exchange.dimodule

import com.andela.currency.data.common.di.NetworkModule
import com.andela.currency.data.exchange.remote.api.ExchangeApi
import com.andela.currency.data.exchange.repository.ExchangeRepositoryImpl
import com.andela.currency.domain.exchange.ExchangeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ExchangeModule {
    @Singleton
    @Provides
    fun provideProductApi(retrofit: Retrofit) : ExchangeApi {
        return retrofit.create(ExchangeApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProductRepository(exchangeApi: ExchangeApi) : ExchangeRepository {
        return ExchangeRepositoryImpl(exchangeApi)
    }
}