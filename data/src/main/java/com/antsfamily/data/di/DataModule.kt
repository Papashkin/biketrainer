package com.antsfamily.data.di

import android.content.Context
import androidx.room.Room
import com.antsfamily.data.local.SharedPrefs
import com.antsfamily.data.local.database.AntsBikeTrainerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Singleton
    @Provides
    fun provideBikeTrainerDatabase(@ApplicationContext appContext: Context): AntsBikeTrainerDatabase =
        Room.databaseBuilder(appContext, AntsBikeTrainerDatabase::class.java, DATABASE_NAME)
            .build()

    @Singleton
    @Provides
    fun provideProfileDao(database: AntsBikeTrainerDatabase) = database.profileDao()

    @Singleton
    @Provides
    fun provideProgramDao(database: AntsBikeTrainerDatabase) = database.programsDao()

    @Singleton
    @Provides
    fun provideKeyValueStorage(@ApplicationContext appContext: Context): SharedPrefs =
        SharedPrefs(appContext)

    private const val DATABASE_NAME = "AntBikeTrainer"
}
