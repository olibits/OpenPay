package com.openpay.android.data.repository
import com.openpay.android.data.local.dao.MovieResponseDao
import com.openpay.android.data.local.dao.TopRatedMovieResponseDao
import com.openpay.android.data.remote.api.WebService
import com.openpay.android.model.movie.MovieResponse
import com.openpay.android.model.movie.TopRatedMoviesResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

interface MovieRepository {
    fun getPopularMovies(): Flow<Resource<MovieResponse>>
    fun getTopRatedMovies(): Flow<Resource<TopRatedMoviesResponse>>
}

@ExperimentalCoroutinesApi
class MovieRepositoryImpl @Inject constructor(
    private val movieResponseDao: MovieResponseDao,
    private val topRelatedMoviesDao: TopRatedMovieResponseDao,
    private val movieService: WebService
) : MovieRepository {

    override fun getPopularMovies(): Flow<Resource<MovieResponse>> {
        return object : NetworkBoundRepository<MovieResponse, MovieResponse>() {
            override suspend fun saveRemoteData(response: MovieResponse) {
                movieResponseDao.addMovieResponse(response)
            }

            override fun fetchFromLocal(): Flow<MovieResponse> {
                return movieResponseDao.getAllMovieResponses()
            }

            override suspend fun fetchFromRemote(): Response<MovieResponse> {
                return movieService.getPopularMovies()
            }
        }.asFlow()
    }

    override fun getTopRatedMovies(): Flow<Resource<TopRatedMoviesResponse>> {
        return object : NetworkBoundRepository<TopRatedMoviesResponse, TopRatedMoviesResponse>() {
            override suspend fun saveRemoteData(response: TopRatedMoviesResponse) {
                topRelatedMoviesDao.addTopMovieResponse(response)
            }

            override fun fetchFromLocal(): Flow<TopRatedMoviesResponse> {
                return topRelatedMoviesDao.getTopMovieResponse()
            }

            override suspend fun fetchFromRemote(): Response<TopRatedMoviesResponse> {
                return movieService.getTopRelatedMovies()
            }
        }.asFlow()
    }
}
