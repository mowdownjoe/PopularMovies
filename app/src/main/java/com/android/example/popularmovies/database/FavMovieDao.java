package com.android.example.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavMovieDao {
    //TODO Add methods to access database
    @Query("SELECT COUNT(*) FROM movies WHERE id = :id")
    int countMovieById(int id); //Checks if movie with this ID exists.

    @Query("SELECT * FROM movies")
    LiveData<List<MovieEntry>> getAllFavorites();

    @Insert
    void addMovieToFavorites(MovieEntry movieEntry);

    @Delete
    void removeMovieFromFavorites(MovieEntry movieEntry);
}
