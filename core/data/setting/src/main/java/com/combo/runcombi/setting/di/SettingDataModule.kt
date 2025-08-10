package com.combo.runcombi.setting.di


import com.combo.runcombi.setting.repository.SettingRepository
import com.combo.runcombi.setting.repository.SettingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SettingDataModule {
    @Binds
    abstract fun bindSettingRepository(impl: SettingRepositoryImpl): SettingRepository
} 