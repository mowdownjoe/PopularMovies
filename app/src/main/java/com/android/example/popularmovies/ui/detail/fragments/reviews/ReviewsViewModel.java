package com.android.example.popularmovies.ui.detail.fragments.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.example.popularmovies.LoadingStatus;
import com.android.example.popularmovies.utils.json.MovieReview;

import java.util.List;

public class ReviewsViewModel extends ViewModel {

    private MutableLiveData<LoadingStatus> status;
    private MutableLiveData<List<MovieReview>> reviews;

    public LiveData<LoadingStatus> getLoadingStatus(){
        return status;
    }

    public LiveData<List<MovieReview>> getReviews(){
        return reviews;
    }

    public ReviewsViewModel() {
        super();
        status = new MutableLiveData<>();
        reviews = new MutableLiveData<>();
        status.postValue(LoadingStatus.INIT);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
    // TODO: Implement the ViewModel
}
