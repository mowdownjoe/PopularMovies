package com.android.example.popularmovies.ui.detail.fragments.videos;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.example.popularmovies.AppExecutors;
import com.android.example.popularmovies.utils.json.MovieVideo;
import com.android.example.popularmovies.utils.network.LoadingStatus;
import com.android.example.popularmovies.utils.network.NetworkUtils;

import java.util.List;

public class VideosViewModel extends ViewModel {

    private MutableLiveData<LoadingStatus> status;
    private MutableLiveData<List<MovieVideo>> videos;

    LiveData<LoadingStatus> getStatus(){
        return status;
    }

    void setStatus(LoadingStatus newStatus) { status.postValue(newStatus); }

    public LiveData<List<MovieVideo>> getVideos(){
        return videos;
    }

    public VideosViewModel() {
        super();
        status = new MutableLiveData<>();
        videos = new MutableLiveData<>();
        status.postValue(LoadingStatus.INIT);
    }

    void fetchVideos(@NonNull String apiKey, int movieId){
        status.postValue(LoadingStatus.LOADING);
        AppExecutors.getInstance().networkIO().execute(() -> {
            List<MovieVideo> fetchedVideos = NetworkUtils.getMovieVideos(movieId, apiKey);
            if (fetchedVideos != null) {
                videos.postValue(fetchedVideos);
                status.postValue(LoadingStatus.DONE);
            } else {
                status.postValue(LoadingStatus.ERROR);
            }
        });
    }
}
