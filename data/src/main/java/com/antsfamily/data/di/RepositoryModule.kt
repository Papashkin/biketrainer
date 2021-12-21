package com.antsfamily.data.di

import com.antsfamily.data.local.repositories.ProfilesRepository
import com.antsfamily.data.local.repositories.ProfilesRepositoryImpl
import com.antsfamily.data.local.repositories.WorkoutRepository
import com.antsfamily.data.local.repositories.WorkoutRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindsProgramsRepository(repositoryImpl: WorkoutRepositoryImpl): WorkoutRepository

    @Binds
    abstract fun bindsProfilesRepository(repositoryImpl: ProfilesRepositoryImpl): ProfilesRepository
}
