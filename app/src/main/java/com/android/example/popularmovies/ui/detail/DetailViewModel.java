package com.android.example.popularmovies.ui.detail;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.example.popularmovies.AppExecutors;
import com.android.example.popularmovies.database.FavMovieDatabase;
import com.android.example.popularmovies.database.MovieEntry;

import java.util.Objects;

public class DetailViewModel extends AndroidViewModel {

    private MutableLiveData<MovieEntry> movie;
    private MutableLiveData<Boolean> isFav;

    public LiveData<MovieEntry> getMovie() {
        return movie;
    }

    public LiveData<Boolean> getIsFav() {
        return isFav;
    }

    public DetailViewModel(Application app, MovieEntry movie){
        super(app);
        this.movie = new MutableLiveData<>();
        this.movie.setValue(movie);
        isFav = new MutableLiveData<>();
        initializeIsFav();
    }

    private boolean isMovieFavorite(){
        int id = Objects.requireNonNull(movie.getValue()).getId();
        return FavMovieDatabase.getInstance(getApplication()).favMovieDao()
                .countMovieById(id) > 0;
    }

    private void initializeIsFav(){
        AppExecutors.getInstance().diskIO().execute(() -> {
            int id = Objects.requireNonNull(movie.getValue()).getId();
            isFav.postValue(FavMovieDatabase.getInstance(getApplication()).favMovieDao()
                    .countMovieById(id) > 0);
        });
    }

    public synchronized void addToFavs(){
        if (isFav.getValue() != null && !isFav.getValue()){
            AppExecutors.getInstance().diskIO().execute(() -> {
                FavMovieDatabase.getInstance(getApplication()).favMovieDao()
                        .addMovieToFavorites(movie.getValue());
                isFav.postValue(true);
            });
        }
    }

    public synchronized void removeFromFavs(){
        if (isFav.getValue() != null && isFav.getValue()){
            AppExecutors.getInstance().diskIO().execute(() -> {
                FavMovieDatabase.getInstance(getApplication()).favMovieDao()
                        .removeMovieFromFavorites(movie.getValue());
                isFav.postValue(false);
            });
        }
    }
}
