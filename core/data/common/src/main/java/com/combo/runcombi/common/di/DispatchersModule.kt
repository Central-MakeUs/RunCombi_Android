package com.combo.runcombi.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatchers: RuncombiDispatcher)

enum class RuncombiDispatcher {
    IO
}

@Module
@InstallIn(SingletonComponent::class)
interface DispatchersModule {

    @Provides
    @Dispatcher(RuncombiDispatcher.IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

}
