package com.android.example.popularmovies.ui.detail;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.example.popularmovies.database.FavMovieDatabase;
import com.android.example.popularmovies.database.FavMovieEntry;

import java.util.Objects;

public class DetailViewModel extends AndroidViewModel {

    private MutableLiveData<FavMovieEntry> movie;
    private MutableLiveData<Boolean> isFav;

    public LiveData<FavMovieEntry> getMovie() {
        return movie;
    }

    public LiveData<Boolean> getIsFav() {
        return isFav;
    }

    public DetailViewModel(Application app, FavMovieEntry movie){
        super(app);
        this.movie = new MutableLiveData<>();
        this.movie.setValue(movie);
        isFav = new MutableLiveData<>();
        isFav.postValue(isMovieFavorite());
    }

    private boolean isMovieFavorite(){
        int id = Objects.requireNonNull(movie.getValue()).getId();
        return FavMovieDatabase.getInstance(getApplication()).favMovieDao()
                .countMovieById(id) > 0;
    }

    public void addToFavs(){
        if (!isFav.getValue()){
            FavMovieDatabase.getInstance(getApplication()).favMovieDao()
                    .addMovieToFavorites(movie.getValue());
            isFav.postValue(true);
        }
    }

    public void removeFromFavs(){
        if (isFav.getValue()){
            FavMovieDatabase.getInstance(getApplication()).favMovieDao()
                    .removeMovieFromFavorites(movie.getValue());
            isFav.postValue(false);
        }
    }
}
