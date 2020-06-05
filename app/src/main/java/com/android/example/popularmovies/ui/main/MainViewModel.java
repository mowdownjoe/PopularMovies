package com.android.example.popularmovies.ui.main;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.example.popularmovies.database.FavMovieDatabase;
import com.android.example.popularmovies.database.MovieEntry;
import com.android.example.popularmovies.utils.json.JsonUtils;
import com.android.example.popularmovies.utils.json.MovieJsonException;
import com.android.example.popularmovies.utils.network.LoadingStatus;
import com.android.example.popularmovies.utils.network.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    enum SortOrder{
        POPULAR("/popular"),
        FAVORITE("fav"),
        TOP_RATED("/top_rated");

        public final String node;

        SortOrder(String node) { this.node = node; }
    }

    private MutableLiveData<SortOrder> sortOrder;
    private MutableLiveData<LoadingStatus> status;
    private MutableLiveData<List<MovieEntry>> moviesFromNetwork;
    private LiveData<List<MovieEntry>> moviesFromDb;
    private MediatorLiveData<List<MovieEntry>> movieList;
    private static FetchMoviesTask fetchMoviesTask;

    LiveData<LoadingStatus> getStatus() {
        return status;
    }

    void setSortOrder(SortOrder newOrder) { sortOrder.postValue(newOrder);}

    LiveData<SortOrder> getSortOrder() { return sortOrder; }

    public void setStatus(LoadingStatus newStatus) {
        status.postValue(newStatus);
    }

    public LiveData<List<MovieEntry>> getMovieList() {
        return movieList;
    }

    public MainViewModel(Application app) {
        super(app);
        status = new MutableLiveData<>();
        sortOrder = new MutableLiveData<>();
        moviesFromNetwork = new MutableLiveData<>();
        moviesFromDb = FavMovieDatabase.getInstance(app).favMovieDao().getAllFavorites();
        movieList = new MediatorLiveData<>();

        status.setValue(LoadingStatus.INIT);

        movieList.addSource(moviesFromNetwork, movieEntries -> movieList.setValue(movieEntries));
    }

    void fetchMovieData(@NonNull String apiKey, SortOrder sortOrder){
        if (sortOrder == SortOrder.FAVORITE){
            throw new IllegalArgumentException();
        }
        movieList.removeSource(moviesFromDb);
        if (fetchMoviesTask != null) {
            fetchMoviesTask.cancel(true);
        }
        fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute(apiKey, sortOrder.node);
    }

    void loadFavoriteMovies(){
        movieList.addSource(moviesFromDb, movieEntries -> movieList.setValue(movieEntries));
        movieList.setValue(moviesFromDb.getValue());
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
                    return JsonUtils.getMovieResultsArray(rawResponse);
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
                try {
                    moviesFromNetwork.postValue(JsonUtils.parseMovieJson(jsonArray));
                    status.postValue(LoadingStatus.DONE);
                } catch (JSONException | IOException e) {
                    status.postValue(LoadingStatus.ERROR);
                    Log.e(getClass().getSimpleName(), "Error parsing movie JSON.", e);
                }
            } else {
                status.postValue(LoadingStatus.ERROR);
            }
        }
    }
}
