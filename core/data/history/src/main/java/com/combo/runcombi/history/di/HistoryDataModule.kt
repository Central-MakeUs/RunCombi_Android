package com.combo.runcombi.history.di

import com.combo.runcombi.history.repository.HistoryRepository
import com.combo.runcombi.history.repository.HistoryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class HistoryDataModule {
    @Binds
    abstract fun bindHistoryRepository(impl: HistoryRepositoryImpl): HistoryRepository
} 