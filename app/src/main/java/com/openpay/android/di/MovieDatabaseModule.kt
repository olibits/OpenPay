package com.openpay.android.di

import android.app.Application
import com.openpay.android.data.local.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MovieDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application) = MovieDatabase.getInstance(application)

    @Singleton
    @Provides
    fun provideMovieDao(database: MovieDatabase) = database.getMovieResponseDao()

    @Singleton
    @Provides
    fun provideTopRatedMovieDao(database: MovieDatabase) = database.getTopRatedMovieResponseDao()

    @Singleton
    @Provides
    fun providePeopleDao(database: MovieDatabase) = database.getPeopleResponseDao()
}
