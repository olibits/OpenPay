package com.openpay.android.model.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openpay.android.model.movie.MovieResponse.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class MovieResponse(
    @PrimaryKey
    var id: Int? = 0,
    val page: Int? = null,
    val results: List<Results> = listOf(),
    val total_pages: Int? = null,
    val total_results: Int? = null
){
    companion object {
        const val TABLE_NAME = "movie_response"
    }
}
