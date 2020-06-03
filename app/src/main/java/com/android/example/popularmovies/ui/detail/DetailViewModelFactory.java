package com.android.example.popularmovies.ui.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.example.popularmovies.database.MovieEntry;

public class DetailViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
    private final MovieEntry mMovieEntry;
    private final Application application;
    private static final Object LOCK = new Object();
    private static DetailViewModelFactory sInstance;

    private DetailViewModelFactory(Application app, MovieEntry movieEntry){
        super(app);
        application = app;
        mMovieEntry = movieEntry;
    }

    public static DetailViewModelFactory getInstance(Application app){
        if (sInstance == null){
            throw new IllegalArgumentException("Instance not created and movie not passed.");
        }
        return sInstance;
    }

    public static DetailViewModelFactory getInstance(Application app, @NonNull MovieEntry movieEntry){
        if (sInstance == null || !sInstance.mMovieEntry.equals(movieEntry)){
            synchronized (LOCK) {
                sInstance = new DetailViewModelFactory(app, movieEntry);
            }
        }
        return sInstance;
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
