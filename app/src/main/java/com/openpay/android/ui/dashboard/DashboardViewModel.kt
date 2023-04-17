package com.openpay.android.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openpay.android.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import com.openpay.android.model.State
import com.openpay.android.model.movie.TopRatedMoviesResponse
import com.openpay.android.model.movie.MovieResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _popularMoviesState = MutableLiveData<State<MovieResponse>>()
    var popularMoviesState: LiveData<State<MovieResponse>> = _popularMoviesState

    private val _topRatedMoviesState = MutableLiveData<State<TopRatedMoviesResponse>>()
    var topRatedMoviesState: LiveData<State<TopRatedMoviesResponse>> = _topRatedMoviesState

    fun getPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.getPopularMovies()
                .onStart { _popularMoviesState.postValue(State.loading()) }
                .map { resource -> State.fromResource(resource) }
                .collect { state -> _popularMoviesState.postValue(state) }
        }
    }

    fun getTopRatedMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.getTopRatedMovies()
                .onStart { _topRatedMoviesState.postValue(State.loading()) }
                .map { resource -> State.fromResource(resource) }
                .collect { state -> _topRatedMoviesState.postValue(state) }
        }
    }
}
