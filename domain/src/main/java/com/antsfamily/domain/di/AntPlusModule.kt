package com.antsfamily.domain.di

import android.content.Context
import com.antsfamily.domain.antservice.AntRadioServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AntPlusModule {

    @Singleton
    @Provides
    fun provideAntRadioServiceConnection(@ApplicationContext appContext: Context) =
        AntRadioServiceConnection(appContext)
}
