package com.android.example.popularmovies.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {MovieEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class FavMovieDatabase extends RoomDatabase {

    private static final String LOG_TAG = FavMovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "fav_movies";
    private static FavMovieDatabase sInstance;

    public static FavMovieDatabase getInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavMovieDatabase.class, FavMovieDatabase.DATABASE_NAME).build();
            }
        }
        Log.d(LOG_TAG, "Returning database instance");
        return sInstance;
    }

    public abstract FavMovieDao favMovieDao();
}
