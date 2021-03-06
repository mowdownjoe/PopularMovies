package com.android.example.popularmovies.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavMovieDao {
    @Query("SELECT COUNT(*) FROM movies WHERE id = :id")
    suspend fun countMovieById(id: Int): Int //Checks if movie with this ID exists.

    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :id)")
    suspend fun doesMovieExist(id: Int): LiveData<Boolean>

    @Query("SELECT * FROM movies")
    fun getAllFavorites(): LiveData<List<MovieEntry>>

    @Insert
    suspend fun addMovieToFavorites(movieEntry: MovieEntry)

    @Delete
    suspend fun removeMovieFromFavorites(movieEntry: MovieEntry)
}