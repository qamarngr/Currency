package com.andela.currency.data.symbols.dimodule

import com.andela.currency.data.common.di.NetworkModule
import com.andela.currency.data.symbols.remote.api.SymbolsApi
import com.andela.currency.data.symbols.repository.SymbolsRepositoryImpl
import com.andela.currency.domain.symbols.SymbolsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class SymbolsModule {
    @Singleton
    @Provides
    fun provideProductApi(retrofit: Retrofit) : SymbolsApi {
        return retrofit.create(SymbolsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProductRepository(productApi: SymbolsApi) : SymbolsRepository {
        return SymbolsRepositoryImpl(productApi)
    }
}