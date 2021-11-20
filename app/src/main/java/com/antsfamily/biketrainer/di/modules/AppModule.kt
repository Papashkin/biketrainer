package com.antsfamily.biketrainer.di.modules

import android.content.Context
import androidx.room.Room
import com.antsfamily.biketrainer.ant.service.AntRadioServiceConnection
import com.antsfamily.biketrainer.data.local.SharedPrefs
import com.antsfamily.biketrainer.data.local.database.AntsBikeTrainerDatabase
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
    fun provideProgramDao(database: AntsBikeTrainerDatabase) = database.programsDao()

    @Singleton
    @Provides
    fun provideKeyValueStorage(@ApplicationContext appContext: Context): SharedPrefs =
        SharedPrefs(appContext)

    @Singleton
    @Provides
    fun provideAntRadioServiceConnection(@ApplicationContext appContext: Context) =
        AntRadioServiceConnection(appContext)
}
