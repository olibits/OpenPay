package com.openpay.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openpay.android.model.movie.TopRatedMoviesResponse
import com.openpay.android.model.people.PeopleResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface PeopleResponseDao {
    /**
     * Inserts [peopleResponse] into the [PeopleResponse.TABLE_NAME] table.
     * Duplicate values are replaced in the table.
     * @param peopleResponse MovieResponse
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPeopleResponse(peopleResponse: PeopleResponse)

    /**
     * Deletes all the movie responses from the [TopRatedMoviesResponse.TABLE_NAME] table.
     */
    @Query("DELETE FROM ${PeopleResponse.TABLE_NAME}")
    suspend fun deleteAllPeopleResponses()

    /**
     * Fetches the movie response from the [PeopleResponse.TABLE_NAME] table whose id is [id].
     * @param id Unique ID of [PeopleResponse]
     * @return [PeopleResponse] of [PeopleResponse] from database table.
     */
    @Query("SELECT * FROM ${PeopleResponse.TABLE_NAME} WHERE ID = :id")
    fun getPeopleResponseById(id: Int): Flow<PeopleResponse>

    /**
     * Fetches all the movie response from the [PeopleResponse.TABLE_NAME] table.
     * @return [Flow]
     */
    @Query("SELECT * FROM ${PeopleResponse.TABLE_NAME}")
    fun getPeopleResponse(): Flow<PeopleResponse>
}