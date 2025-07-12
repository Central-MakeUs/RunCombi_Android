package com.combo.runcombi.data.user.di

import com.combo.runcombi.data.user.repository.UserRepositoryImpl
import com.combo.runcombi.domain.user.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UserDataModule {
    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
} 