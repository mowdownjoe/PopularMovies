package com.android.example.popularmovies.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.popularmovies.LoadingStatus;
import com.android.example.popularmovies.R;
import com.android.example.popularmovies.databinding.ActivityMainBinding;
import com.android.example.popularmovies.ui.PosterAdapter;
import com.android.example.popularmovies.utils.MovieJsonException;
import com.android.example.popularmovies.utils.NetworkUtils;
import com.android.example.popularmovies.utils.json.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PosterAdapter.PosterOnClickListener {

    ActivityMainBinding binding;
    MainViewModel viewModel;
    PosterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding.rvMoviePosterGrid.setHasFixedSize(true);

        //TODO Set spanCount based on orientation and shortest width.
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        binding.rvMoviePosterGrid.setLayoutManager(layoutManager);

        adapter = new PosterAdapter(this);
        binding.rvMoviePosterGrid.setAdapter(adapter);

        viewModel.getStatus().observe(this, loadingStatus -> {
            if (loadingStatus == LoadingStatus.LOADING){
                binding.pbLoadingSpinner.setVisibility(View.VISIBLE);
                binding.rvMoviePosterGrid.setVisibility(View.INVISIBLE);
                binding.tvErrorText.setVisibility(View.INVISIBLE);
            } else if (loadingStatus == LoadingStatus.ERROR){
                binding.tvErrorText.setVisibility(View.VISIBLE);
                binding.rvMoviePosterGrid.setVisibility(View.INVISIBLE);
                binding.pbLoadingSpinner.setVisibility(View.INVISIBLE);
            } else if (loadingStatus == LoadingStatus.DONE){
                binding.rvMoviePosterGrid.setVisibility(View.VISIBLE);
                binding.tvErrorText.setVisibility(View.INVISIBLE);
                binding.pbLoadingSpinner.setVisibility(View.INVISIBLE);
            }
        });

        viewModel.getMovieData().observe(this, movieData -> {
            if (movieData != null){
                adapter.setMovieData(movieData);
            }
        });

        loadMovieData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_order_menu, menu);
        menu.findItem(R.id.sort_popular_mi).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        if (item.getItemId() == R.id.sort_popular_mi){
            loadMovieData(NetworkUtils.POPULAR_NODE);
            return true;
        } else if (item.getItemId() == R.id.sort_highest_rated_mi){
            loadMovieData(NetworkUtils.TOP_RATED_NODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovieData(){
        loadMovieData(NetworkUtils.POPULAR_NODE);
    }

    private void loadMovieData(String sortOrder) {
        viewModel.fetchMovieData(getString(R.string.api_key_v3), sortOrder);
    }

    @Override
    public void onListItemClick(JSONObject movieData) {
        Log.v("MainActivity", "Received click on list item.");
        try {
            startActivity(JsonUtils.buildMovieIntent(movieData, this));
        } catch (JSONException | MovieJsonException e) {
            e.printStackTrace();
        }
    }

}
