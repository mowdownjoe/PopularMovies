package com.android.example.popularmovies.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.example.popularmovies.database.MovieEntry

@Database(entities = [MovieEntry::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class FavMovieDatabase : RoomDatabase() {
    abstract fun favMovieDao(): FavMovieDao

    companion object {
        private val LOG_TAG = FavMovieDatabase::class.java.simpleName
        private val LOCK = Any()
        private const val DATABASE_NAME = "fav_movies"
        private var sInstance: FavMovieDatabase? = null
        fun getInstance(context: Context): FavMovieDatabase? {
            if (sInstance == null) {
                synchronized(LOCK) {
                    Log.d(LOG_TAG, "Creating new database instance")
                    sInstance = Room.databaseBuilder(context.applicationContext,
                            FavMovieDatabase::class.java, DATABASE_NAME).build()
                }
            }
            Log.d(LOG_TAG, "Returning database instance")
            return sInstance
        }
    }
}