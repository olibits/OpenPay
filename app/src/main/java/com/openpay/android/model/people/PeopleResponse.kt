package com.openpay.android.model.people

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = PeopleResponse.TABLE_NAME)
data class PeopleResponse(
    @PrimaryKey
    var id: Int? = 0,
    @SerializedName("page") val page: Int? = null,
    @SerializedName("results") val results: List<PeopleResult>? = listOf(),
    @SerializedName("total_pages") val totalPages: Int? = null,
    @SerializedName("total_results") val totalResults: Int? = null

) {
    companion object {
        const val TABLE_NAME = "people_response"
    }
}
