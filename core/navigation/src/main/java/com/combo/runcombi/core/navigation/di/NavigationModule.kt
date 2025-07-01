package com.combo.runcombi.core.navigation.di

import com.combo.runcombi.core.navigation.AuthNavigationEventHandler
import com.combo.runcombi.core.navigation.NavigationEventHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class AuthNavigation

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @AuthNavigation
    @Binds
    @Singleton
    abstract fun bindAuthNavigationEventHandler(
        impl: AuthNavigationEventHandler,
    ): NavigationEventHandler
}
