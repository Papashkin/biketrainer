package com.antsfamily.biketrainer.di.modules

import com.antsfamily.biketrainer.data.local.repositories.ProfilesRepository
import com.antsfamily.biketrainer.data.local.repositories.ProfilesRepositoryImpl
import com.antsfamily.biketrainer.data.local.repositories.ProgramsRepository
import com.antsfamily.biketrainer.data.local.repositories.ProgramsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {

    @Binds
    abstract fun bindsProgramsRepository(repositoryImpl: ProgramsRepositoryImpl): ProgramsRepository

    @Binds
    abstract fun bindsProfilesRepository(repositoryImpl: ProfilesRepositoryImpl): ProfilesRepository
}
