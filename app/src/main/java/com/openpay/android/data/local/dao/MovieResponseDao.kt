package com.openpay.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.openpay.android.model.movie.MovieResponse

@Dao
interface MovieResponseDao {
    /**
     * Inserts [movieResponse] into the [MovieResponse.TABLE_NAME] table.
     * Duplicate values are replaced in the table.
     * @param movieResponse MovieResponse
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieResponse(movieResponse: MovieResponse)

    /**
     * Deletes all the movie responses from the [MovieResponse.TABLE_NAME] table.
     */
    @Query("DELETE FROM ${MovieResponse.TABLE_NAME}")
    suspend fun deleteAllMovieResponse()

    /**
     * Fetches the movie response from the [MovieResponse.TABLE_NAME] table whose id is [id].
     * @param id Unique ID of [MovieResponse]
     * @return [MovieResponse] of [MovieResponse] from database table.
     */
    @Query("SELECT * FROM ${MovieResponse.TABLE_NAME} WHERE ID = :id")
    fun getMovieResponseById(id: Int): Flow<MovieResponse>

    /**
     * Fetches all the movie response from the [MovieResponse.TABLE_NAME] table.
     * @return [Flow]
     */
    @Query("SELECT * FROM ${MovieResponse.TABLE_NAME}")
    fun getAllMovieResponses(): Flow<MovieResponse>
}