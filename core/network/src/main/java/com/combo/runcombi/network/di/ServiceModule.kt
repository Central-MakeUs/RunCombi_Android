package com.goalpanzi.mission_mate.core.network.di

import com.combo.runcombi.network.di.TokenReissueRetrofit
import com.combo.runcombi.network.di.TokenRetrofit
import com.combo.runcombi.network.service.HistoryService
import com.combo.runcombi.network.service.AuthService
import com.combo.runcombi.network.service.SettingService
import com.combo.runcombi.network.service.TokenService
import com.combo.runcombi.network.service.UserService
import com.combo.runcombi.network.service.WalkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(
        @TokenRetrofit retrofit: Retrofit,
    ): AuthService {
        return retrofit.create(AuthService::class.java)
    }


    @Provides
    @Singleton
    fun provideTokenService(
        @TokenReissueRetrofit retrofit: Retrofit,
    ): TokenService {
        return retrofit.create(TokenService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(
        @TokenRetrofit retrofit: Retrofit,
    ): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideWalkService(
        @TokenRetrofit retrofit: Retrofit,
    ): WalkService {
        return retrofit.create(WalkService::class.java)
    }

    @Provides
    @Singleton
    fun provideHistoryService(
        @TokenRetrofit retrofit: Retrofit,
    ): HistoryService {
        return retrofit.create(HistoryService::class.java)
    }

    @Provides
    @Singleton
    fun provideSettingService(
        @TokenRetrofit retrofit: Retrofit,
    ): SettingService {
        return retrofit.create(SettingService::class.java)
    }

}
