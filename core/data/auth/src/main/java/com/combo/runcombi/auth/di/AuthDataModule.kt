package com.combo.runcombi.auth.di

import com.combo.runcombi.auth.AuthTokenExpirationHandler
import com.combo.runcombi.auth.AuthTokenProvider
import com.combo.runcombi.auth.repository.AuthRepository
import com.combo.runcombi.auth.repository.AuthRepositoryImpl
import com.combo.runcombi.network.TokenExpirationHandler
import com.combo.runcombi.network.TokenProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AuthDataModule {

    @Binds
    abstract fun bindLoginRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindTokenProvider(impl: AuthTokenProvider): TokenProvider

    @Binds
    abstract fun bindTokenExpirationHandler(impl: AuthTokenExpirationHandler): TokenExpirationHandler
}
