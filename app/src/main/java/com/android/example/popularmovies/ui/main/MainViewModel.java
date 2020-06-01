package com.android.example.popularmovies.ui.main;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.example.popularmovies.utils.json.JsonUtils;
import com.android.example.popularmovies.utils.json.MovieJsonException;
import com.android.example.popularmovies.utils.network.LoadingStatus;
import com.android.example.popularmovies.utils.network.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainViewModel extends ViewModel {

    private MutableLiveData<LoadingStatus> status;
    private MutableLiveData<JSONArray> movieData;
    private static FetchMoviesTask fetchMoviesTask;

    LiveData<LoadingStatus> getStatus() {
        return status;
    }

    public void setStatus(LoadingStatus newStatus) {
        status.postValue(newStatus);
    }

    LiveData<JSONArray> getMovieData() {
        return movieData;
    }

    void fetchMovieData(@NonNull String apiKey, String sortOrder){
        if (fetchMoviesTask != null) {
            fetchMoviesTask.cancel(true);
        }
        fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute(apiKey, sortOrder);
    }

    public MainViewModel() {
        super();
        status = new MutableLiveData<>();
        movieData = new MutableLiveData<>();
        status.setValue(LoadingStatus.INIT);
    }

    @Override
    protected void onCleared() {
        if (fetchMoviesTask != null) {
            fetchMoviesTask.cancel(true);
        }
        super.onCleared();
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchMoviesTask extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            status.postValue(LoadingStatus.LOADING);
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            if (params.length != 2) {
                return null;
            }

            String apiKey = params[0];
            String sortOrder = params[1];

            try {
                JSONObject rawResponse = NetworkUtils.getMovies(sortOrder, apiKey);
                if (rawResponse != null) {
                    return JsonUtils.getResultsArray(rawResponse);
                } else {
                    return null;
                }
            } catch (IOException | JSONException | MovieJsonException e) {
                Log.e("FetchMovieTask", "Error caught while fetching and parsing JSON", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);

            if (jsonArray != null) {
                movieData.postValue(jsonArray);
                status.postValue(LoadingStatus.DONE);
            } else {
                status.postValue(LoadingStatus.ERROR);
            }
        }
    }
}
