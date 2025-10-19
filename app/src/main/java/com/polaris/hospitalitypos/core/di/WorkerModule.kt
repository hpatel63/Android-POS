package com.polaris.hospitalitypos.core.di

import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkManagerConfigModule {
    @Provides
    @Singleton
    fun provideWorkManagerConfiguration(factory: HiltWorkerFactory): Configuration =
        Configuration.Builder().setWorkerFactory(factory).build()
}
