package com.openpay.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import androidx.room.TypeConverters
import com.openpay.android.data.local.dao.MovieResponseDao
import com.openpay.android.data.local.dao.PeopleResponseDao
import com.openpay.android.data.local.dao.TopRatedMovieResponseDao
import com.openpay.android.model.movie.TopRatedMoviesResponse
import com.openpay.android.model.movie.MovieResponse
import com.openpay.android.model.people.PeopleResponse
import com.openpay.android.utils.Converters

/**
 * Abstract Movie database.
 * It provides DAO [MovieResponseDao] by using method [getMovieResponseDao].
 */
@Database(
    entities = [MovieResponse::class, TopRatedMoviesResponse::class, PeopleResponse::class],
    version = DatabaseMigrations.DB_VERSION
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {

    /**
     * @return [MovieResponseDao] Movie Data Access Object.
     */
    abstract fun getMovieResponseDao(): MovieResponseDao

    /**
     * @return [TopRatedMovieResponseDao] TopRatedMovieResponse Data Access Object.
     */
    abstract fun getTopRatedMovieResponseDao(): TopRatedMovieResponseDao

    /**
     * @return [PeopleResponseDao] PeopleResponse Data Access Object.
     */
    abstract fun getPeopleResponseDao(): PeopleResponseDao

    companion object {
        const val DB_NAME = "movie_database"

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    DB_NAME
                ).addMigrations(*DatabaseMigrations.MIGRATIONS).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
