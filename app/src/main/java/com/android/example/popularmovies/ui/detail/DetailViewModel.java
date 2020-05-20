package com.android.example.popularmovies.ui.detail;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.example.popularmovies.database.FavMovieDatabase;
import com.android.example.popularmovies.database.FavMovieEntry;

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
        /*isFav = new MutableLiveData<>();
        isFav.postValue(isMovieFavorite());*/
    }

    private boolean isMovieFavorite(){
        return FavMovieDatabase.getInstance(getApplication()).favMovieDao()
                .countMovieById(movie.getValue().getId()) > 0;
    }
}
