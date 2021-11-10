package com.antsfamily.biketrainer.di.modules

import android.content.Context
import androidx.room.Room
import com.antsfamily.biketrainer.ant.service.AntRadioServiceConnection
import com.antsfamily.biketrainer.data.local.database.AntsBikeTrainerDatabase
import com.antsfamily.biketrainer.data.local.database.ProfileDao
import com.antsfamily.biketrainer.data.local.database.ProgramDao
import com.antsfamily.biketrainer.data.local.repositories.ProfilesRepository
import com.antsfamily.biketrainer.data.local.repositories.ProgramsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideBikeTrainerDatabase(@ApplicationContext appContext: Context): AntsBikeTrainerDatabase =
        Room.databaseBuilder(appContext, AntsBikeTrainerDatabase::class.java, "AntBikeTrainer")
            .build()

    @Singleton
    @Provides
    fun provideProfileDao(database: AntsBikeTrainerDatabase) = database.profileDao()

    @Singleton
    @Provides
    fun provideProfilesRepository(dao: ProfileDao) = ProfilesRepository(dao)

    @Singleton
    @Provides
    fun provideProgramDao(database: AntsBikeTrainerDatabase) = database.programsDao()

    @Singleton
    @Provides
    fun provideProgramsRepository(dao: ProgramDao) = ProgramsRepository(dao)

    @Singleton
    @Provides
    fun provideAntRadioServiceConnection(@ApplicationContext appContext: Context) = AntRadioServiceConnection(appContext)
}
