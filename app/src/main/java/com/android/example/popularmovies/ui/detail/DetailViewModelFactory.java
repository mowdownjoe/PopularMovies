package com.android.example.popularmovies.ui.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.example.popularmovies.database.FavMovieEntry;

public class DetailViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
    private final FavMovieEntry mMovieEntry;
    private final Application application;

    public DetailViewModelFactory(Application app, FavMovieEntry movieEntry){
        super(app);
        application = app;
        mMovieEntry = movieEntry;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DetailViewModel.class)) {
            return (T) new DetailViewModel(application, mMovieEntry);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
