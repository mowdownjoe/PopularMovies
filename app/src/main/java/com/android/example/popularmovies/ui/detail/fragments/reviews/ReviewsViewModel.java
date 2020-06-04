package com.android.example.popularmovies.ui.detail.fragments.reviews;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.example.popularmovies.AppExecutors;
import com.android.example.popularmovies.utils.json.MovieReview;
import com.android.example.popularmovies.utils.network.LoadingStatus;
import com.android.example.popularmovies.utils.network.NetworkUtils;

import java.util.List;

public class ReviewsViewModel extends ViewModel {

    private MutableLiveData<LoadingStatus> status;
    private MutableLiveData<List<MovieReview>> reviews;

    LiveData<LoadingStatus> getStatus(){
        return status;
    }

    @SuppressWarnings("SameParameterValue")
    void setStatus(LoadingStatus newStatus) {
        status.postValue(newStatus);
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

    void fetchReviews(@NonNull String apiKey, int movieId){
        status.postValue(LoadingStatus.LOADING);
        AppExecutors.getInstance().networkIO().execute(() -> {
            List<MovieReview> fetchedReviews = NetworkUtils.getMovieReviews(movieId, apiKey);
            if (fetchedReviews != null){
                reviews.postValue(fetchedReviews);
                status.postValue(LoadingStatus.DONE);
            } else {
                status.postValue(LoadingStatus.ERROR);
            }
        });
    }
}
