package com.android.example.popularmovies.ui.detail;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.example.popularmovies.database.FavMovieDatabase;
import com.android.example.popularmovies.database.FavMovieEntry;

public class DetailViewModel extends AndroidViewModel {

    private MutableLiveData<FavMovieEntry> movie;

    public MutableLiveData<FavMovieEntry> getMovie() {
        return movie;
    }

    public DetailViewModel(Application app, FavMovieEntry movie){
        super(app);
        this.movie = new MutableLiveData<>();
        this.movie.setValue(movie);
    }

    private boolean isMovieFavorite(){
        return FavMovieDatabase.getInstance(getApplication()).favMovieDao()
                .countMovieById(movie.getValue().getId()) > 0;
    }
}
