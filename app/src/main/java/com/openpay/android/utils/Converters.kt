package com.openpay.android.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.openpay.android.model.movie.Results
import com.openpay.android.model.people.PeopleResult
import java.lang.reflect.Type
import java.util.*


class Converters {

    /**
     * Movie Response converter
     * */
    @TypeConverter
    fun jsonToMyMovieResponse(data: String?): List<Results?>? {
        val gson = Gson()
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Results?>?>() {}.getType()
        return gson.fromJson<List<Results?>>(data, listType)
    }
    /**
     * Movie Response converter
     * */
    @TypeConverter
    fun myMovieResponseToJson(myObjects: List<Results?>?): String? {
        val gson = Gson()
        return gson.toJson(myObjects)
    }

    /**
     * Movie Response converter
     * */
    @TypeConverter
    fun jsonToMyPeopleResponse(data: String?): List<PeopleResult?>? {
        val gson = Gson()
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<PeopleResult?>?>() {}.getType()
        return gson.fromJson<List<PeopleResult?>>(data, listType)
    }
    /**
     * Movie Response converter
     * */
    @TypeConverter
    fun myPeopleResponseToJson(myObjects: List<PeopleResult?>?): String? {
        val gson = Gson()
        return gson.toJson(myObjects)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID): String?{
        return uuid.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String): UUID? {
        return UUID.fromString(uuid)
    }
}
