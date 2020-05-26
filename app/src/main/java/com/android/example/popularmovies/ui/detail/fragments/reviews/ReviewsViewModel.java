package com.android.example.popularmovies.ui.detail.fragments.reviews;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.example.popularmovies.LoadingStatus;
import com.android.example.popularmovies.utils.json.MovieReview;

import java.util.List;

public class ReviewsViewModel extends ViewModel {

    private MutableLiveData<LoadingStatus> status;
    private MutableLiveData<List<MovieReview>> reviews;
    private static FetchReviewsTask fetchReviewsTask;

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

    void fetchReviews(@NonNull String apiKey, int movieId){
        if (fetchReviewsTask != null){
            fetchReviewsTask.cancel(true);
        }
        fetchReviewsTask = new FetchReviewsTask();
        fetchReviewsTask.execute(apiKey, Integer.toString(movieId));
    }

    @Override
    protected void onCleared() {
        if (fetchReviewsTask != null) {
            fetchReviewsTask.cancel(true);
        }
        super.onCleared();
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchReviewsTask extends AsyncTask<String, Void, List<MovieReview>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            status.postValue(LoadingStatus.LOADING);
        }

        @Override
        protected List<MovieReview> doInBackground(String... strings) {
            //TODO Fetch reviews
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieReview> movieReviews) {
            super.onPostExecute(movieReviews);

            if (movieReviews != null){
                reviews.postValue(movieReviews);
                status.postValue(LoadingStatus.DONE);
            } else {
                status.postValue(LoadingStatus.ERROR);
            }
        }
    }
}
