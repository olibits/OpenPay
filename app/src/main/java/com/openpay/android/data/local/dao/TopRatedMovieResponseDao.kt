package com.openpay.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openpay.android.model.movie.TopRatedMoviesResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface TopRatedMovieResponseDao {
    /**
     * Inserts [topRatedMoviesResponse] into the [TopRatedMoviesResponse.TABLE_NAME] table.
     * Duplicate values are replaced in the table.
     * @param topRatedMoviesResponse MovieResponse
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTopMovieResponse(topRatedMoviesResponse: TopRatedMoviesResponse)

    /**
     * Deletes all the movie responses from the [TopRatedMoviesResponse.TABLE_NAME] table.
     */
    @Query("DELETE FROM ${TopRatedMoviesResponse.TABLE_NAME}")
    suspend fun deleteAllTopMovieResponses()

    /**
     * Fetches the movie response from the [TopRatedMoviesResponse.TABLE_NAME] table whose id is [id].
     * @param id Unique ID of [TopRatedMoviesResponse]
     * @return [TopRatedMoviesResponse] of [TopRatedMoviesResponse] from database table.
     */
    @Query("SELECT * FROM ${TopRatedMoviesResponse.TABLE_NAME} WHERE ID = :id")
    fun getTopMovieResponseById(id: Int): Flow<TopRatedMoviesResponse>

    /**
     * Fetches all the movie response from the [TopRatedMoviesResponse.TABLE_NAME] table.
     * @return [Flow]
     */
    @Query("SELECT * FROM ${TopRatedMoviesResponse.TABLE_NAME}")
    fun getTopMovieResponse(): Flow<TopRatedMoviesResponse>
}