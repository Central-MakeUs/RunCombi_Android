package com.goalpanzi.mission_mate.core.network.di

import com.combo.runcombi.network.di.TokenReissueRetrofit
import com.combo.runcombi.network.di.TokenRetrofit
import com.combo.runcombi.network.service.LoginService
import com.combo.runcombi.network.service.TokenService
import com.combo.runcombi.network.service.UserService
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
    fun provideLoginService(
        @TokenRetrofit retrofit: Retrofit,
    ): LoginService {
        return retrofit.create(LoginService::class.java)
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
    ): UserService{
        return retrofit.create(UserService::class.java)
    }

}
