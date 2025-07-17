package com.combo.runcombi.network.di


import com.combo.runcombi.network.BuildConfig
import com.combo.runcombi.network.interceptor.TokenInterceptor
import com.combo.runcombi.network.interceptor.TokenReissueInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class TokenInterceptorHttpClient

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class TokenReissueInterceptorHttpClient

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class TokenRetrofit

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class TokenReissueRetrofit

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideRequestHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @TokenInterceptorHttpClient
    @Provides
    @Singleton
    fun provideTokenInterceptorHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        tokenInterceptor: TokenInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @TokenReissueInterceptorHttpClient
    @Provides
    @Singleton
    fun provideTokenReissueInterceptorHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        tokenReissueInterceptor: TokenReissueInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(tokenReissueInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideConverterFactory(
        json: Json,
    ): Converter.Factory {
        return json.asConverterFactory("application/json".toMediaType())
    }

    @TokenRetrofit
    @Provides
    @Singleton
    fun provideTokenRetrofit(
        @TokenInterceptorHttpClient okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(converterFactory)
            .build()
    }

    @TokenReissueRetrofit
    @Provides
    @Singleton
    fun provideTokenReissueRetrofit(
        @TokenReissueInterceptorHttpClient okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(converterFactory)
            .build()
    }
}
