package com.antsfamily.biketrainer.di.modules

import android.content.Context
import com.antsfamily.biketrainer.util.AppContentResolver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppContentModule {

    @Singleton
    @Provides
    fun provideContentResolver(@ApplicationContext appContext: Context): AppContentResolver =
        AppContentResolver(appContext)
}
