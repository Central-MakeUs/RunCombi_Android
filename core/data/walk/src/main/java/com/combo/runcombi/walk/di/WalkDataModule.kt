package com.combo.runcombi.walk.di

import com.combo.runcombi.walk.repository.WalkRepository
import com.combo.runcombi.walk.repository.WalkRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class WalkDataModule {
    @Binds
    abstract fun bindWalkRepository(impl: WalkRepositoryImpl): WalkRepository
} 