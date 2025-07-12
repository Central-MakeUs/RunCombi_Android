package com.combo.runcombi.pet.di

import com.combo.runcombi.pet.repository.PetRepository
import com.combo.runcombi.pet.repository.PetRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class PetDataModule {
    @Binds
    abstract fun bindPetRepository(impl: PetRepositoryImpl): PetRepository
} 