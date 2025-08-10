package com.combo.runcombi.data.setting.di

import com.combo.runcombi.data.setting.repository.MockSettingRepositoryImpl
import com.combo.runcombi.setting.repository.SettingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SettingDataModule {
    @Binds
    abstract fun bindSettingRepository(impl: MockSettingRepositoryImpl): SettingRepository
}
