package com.openpay.android.model.movie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TopRatedMoviesResponse.TABLE_NAME)
data class TopRatedMoviesResponse(
    @PrimaryKey
    var id: Int? = 0,
    val page: Int? = null,
    val results: List<Results> = listOf(),
    val total_pages: Int? = null,
    val total_results: Int? = null
) {
    companion object {
        const val TABLE_NAME = "top_movie_response"
    }
}
