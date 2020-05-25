package com.android.example.popularmovies.ui.detail.fragments.videos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.example.popularmovies.LoadingStatus;
import com.android.example.popularmovies.utils.json.MovieVideo;

import java.util.List;

public class VideosViewModel extends ViewModel {

    private MutableLiveData<LoadingStatus> status;
    private MutableLiveData<List<MovieVideo>> videos;

    public LiveData<LoadingStatus> getStatus(){
        return status;
    }

    public LiveData<List<MovieVideo>> getVideos(){
        return videos;
    }

    public VideosViewModel() {
        super();
        status = new MutableLiveData<>();
        videos = new MutableLiveData<>();
        status.postValue(LoadingStatus.INIT);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
    // TODO: Implement the ViewModel
}
