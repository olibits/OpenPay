package com.openpay.android.di

import com.openpay.android.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Currently Repository is only used in ViewModels.
 * ViewModel is not injected using @HiltViewModel so can't install in ViewModelComponent.
 */
@ExperimentalCoroutinesApi
@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class RepositoryModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun bindMovieRepository(repository: MovieRepositoryImpl): MovieRepository

    @ActivityRetainedScoped
    @Binds
    abstract fun bindPeopleRepository(repository: PeopleRepositoryImpl): PeopleRepository

    @ActivityRetainedScoped
    @Binds
    abstract fun bindImageRepository(repository: ImagesRepositoryImpl): ImagesRepository
}
