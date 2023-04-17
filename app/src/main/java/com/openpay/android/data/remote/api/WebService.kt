package com.openpay.android.data.remote.api

import com.openpay.android.model.movie.TopRatedMoviesResponse
import com.openpay.android.model.movie.MovieResponse
import com.openpay.android.model.people.PeopleResponse
import retrofit2.Response
import retrofit2.http.GET

interface WebService {

    @GET("3/movie/popular?api_key=bf7ebd1a71743567e18e23384c90bddb&language=en-US&page=1")
    suspend fun getPopularMovies(): Response<MovieResponse>

    @GET("3/movie/top_rated?api_key=bf7ebd1a71743567e18e23384c90bddb&language=en-US&page=1")
    suspend fun getTopRelatedMovies(): Response<TopRatedMoviesResponse>

    @GET("3/person/popular?api_key=bf7ebd1a71743567e18e23384c90bddb&language=en-US&page=1")
    suspend fun getPeople(): Response<PeopleResponse>

    companion object {
        const val MOVIE_API_URL = "https://api.themoviedb.org/"
    }
}