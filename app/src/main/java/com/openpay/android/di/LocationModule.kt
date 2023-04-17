package com.openpay.android.di

import android.content.Context
import com.openpay.android.data.local.MyLocationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocationModule {
    @Singleton
    @Provides
    fun providesLocationManager(
        @ApplicationContext context: Context
    ): MyLocationManager {
        return MyLocationManager(context)
    }
}