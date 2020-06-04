package com.android.example.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavMovieDao {

    @Query("SELECT COUNT(*) FROM movies WHERE id = :id")
    int countMovieById(int id); //Checks if movie with this ID exists.

    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :id)")
    LiveData<Boolean> doesMovieExist(int id);

    @Query("SELECT * FROM movies")
    List<MovieEntry> getAllFavorites();

    @Insert
    void addMovieToFavorites(MovieEntry movieEntry);

    @Delete
    void removeMovieFromFavorites(MovieEntry movieEntry);
}
