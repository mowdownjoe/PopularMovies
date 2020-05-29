package com.android.example.popularmovies.ui.detail.fragments.videos;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.example.popularmovies.LoadingStatus;
import com.android.example.popularmovies.utils.json.MovieVideo;
import com.android.example.popularmovies.utils.network.NetworkUtils;

import java.util.List;

public class VideosViewModel extends ViewModel {

    private static FetchVideosTask fetchVideosTask;
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

    @Override
    protected void onCleared() {
        super.onCleared();
        if (fetchVideosTask != null){
            fetchVideosTask.cancel(true);
        }
    }

    void fetchVideos(@NonNull String apiKey, int movieId){
        if (fetchVideosTask != null){ //TODO Switch to AppExecutor?
            fetchVideosTask.cancel(true);
        }
        fetchVideosTask = new FetchVideosTask();
        fetchVideosTask.execute(apiKey, Integer.toString(movieId));
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchVideosTask extends AsyncTask<String, Void, List<MovieVideo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            status.postValue(LoadingStatus.LOADING);
        }

        @Override
        protected List<MovieVideo> doInBackground(String... params) {
            //TODO Debug why videos aren't loading

            if (params.length != 2){
                return null;
            }

            String apiKey = params[0];
            int movieId = Integer.parseInt(params[1]);

            return NetworkUtils.getMovieVideos(movieId, apiKey);
        }

        @Override
        protected void onPostExecute(List<MovieVideo> movieVideos) {
            super.onPostExecute(movieVideos);

            if (movieVideos != null){
                videos.postValue(movieVideos);
                status.postValue(LoadingStatus.DONE);
            } else {
                status.postValue(LoadingStatus.ERROR);
            }
        }
    }
}
