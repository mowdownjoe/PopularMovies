package com.android.example.popularmovies;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.popularmovies.databinding.ActivityMainBinding;
import com.android.example.popularmovies.utils.JsonUtils;
import com.android.example.popularmovies.utils.MovieJsonException;
import com.android.example.popularmovies.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements PosterAdapter.PosterOnClickListener {

    ActivityMainBinding binding;
    PosterAdapter adapter;
    private static FetchMoviesTask fetchMoviesTask;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_order_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (fetchMoviesTask != null){
            fetchMoviesTask.cancel(true);
        }
        if (item.getItemId() == R.id.sort_popular_mi){
            loadMovieData(NetworkUtils.POPULAR_NODE);
            return true;
        } else if (item.getItemId() == R.id.sort_highest_rated_mi){
            loadMovieData(NetworkUtils.TOP_RATED_NODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rvMoviePosterGrid.setHasFixedSize(true);

        //TODO Set spanCount based on orientation and shortest width.
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        binding.rvMoviePosterGrid.setLayoutManager(layoutManager);

        adapter = new PosterAdapter(this);
        binding.rvMoviePosterGrid.setAdapter(adapter);

        loadMovieData();
    }

    private void loadMovieData(){
        loadMovieData(NetworkUtils.POPULAR_NODE);
    }

    private void loadMovieData(String sortOrder) {
        showPosterList();
        fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute(getString(R.string.api_key_v3), sortOrder);
    }

    private void showPosterList(){
        binding.rvMoviePosterGrid.setVisibility(View.VISIBLE);
        binding.tvErrorText.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage(){
        binding.tvErrorText.setVisibility(View.VISIBLE);
        binding.rvMoviePosterGrid.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onListItemClick(JSONObject movieData) {
        /*try {
            startActivity(JsonUtils.buildMovieIntent(movieData, this));
        } catch (JSONException | MovieJsonException e) {
            e.printStackTrace();
        }*/
    }

    @SuppressLint("StaticFieldLeak")
    class FetchMoviesTask extends AsyncTask<String, Void, JSONArray>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.pbLoadingSpinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            binding.pbLoadingSpinner.setVisibility(View.INVISIBLE);
            if (jsonArray != null){
                adapter.setMovieData(jsonArray);
                showPosterList();
            } else {
                showErrorMessage();
            }
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
    }
}
