package com.android.example.popularmovies.ui.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.android.example.popularmovies.database.FavMovieEntry;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final FavMovieEntry mMovieEntry;

    public DetailViewModelFactory(FavMovieEntry movieEntry){
        mMovieEntry = movieEntry;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DetailViewModel.class)) {
            return (T) new DetailViewModel(mMovieEntry);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
