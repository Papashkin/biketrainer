package com.antsfamily.data.di

import com.antsfamily.data.local.repositories.ProfilesRepository
import com.antsfamily.data.local.repositories.ProfilesRepositoryImpl
import com.antsfamily.data.local.repositories.ProgramsRepository
import com.antsfamily.data.local.repositories.ProgramsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindsProgramsRepository(repositoryImpl: ProgramsRepositoryImpl): ProgramsRepository

    @Binds
    abstract fun bindsProfilesRepository(repositoryImpl: ProfilesRepositoryImpl): ProfilesRepository
}
