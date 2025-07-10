package com.combo.runcombi.datastore.di

import com.combo.runcombi.datastore.datasource.AuthDataSource
import com.combo.runcombi.datastore.datasource.AuthDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindAuthDataSource(
        authDataSource: AuthDataSourceImpl,
    ): AuthDataSource

}