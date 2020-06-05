package com.android.example.popularmovies.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.popularmovies.R;
import com.android.example.popularmovies.database.MovieEntry;
import com.android.example.popularmovies.databinding.ActivityMainBinding;
import com.android.example.popularmovies.ui.detail.DetailActivity;
import com.android.example.popularmovies.utils.json.JsonUtils;

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

        GridAutofitLayoutManager layoutManager =
                new GridAutofitLayoutManager(this, 490, RecyclerView.VERTICAL, false);
        binding.rvMoviePosterGrid.setLayoutManager(layoutManager);

        adapter = new PosterAdapter(this);
        binding.rvMoviePosterGrid.setAdapter(adapter);

        viewModel.getStatus().observe(this, loadingStatus -> {
            switch (loadingStatus){
                case LOADING:
                    binding.pbLoadingSpinner.setVisibility(View.VISIBLE);
                    binding.rvMoviePosterGrid.setVisibility(View.INVISIBLE);
                    binding.tvErrorText.setVisibility(View.INVISIBLE);
                    break;
                case DONE:
                    binding.rvMoviePosterGrid.setVisibility(View.VISIBLE);
                    binding.tvErrorText.setVisibility(View.INVISIBLE);
                    binding.pbLoadingSpinner.setVisibility(View.INVISIBLE);
                    break;
                case ERROR:
                    binding.tvErrorText.setVisibility(View.VISIBLE);
                    binding.rvMoviePosterGrid.setVisibility(View.INVISIBLE);
                    binding.pbLoadingSpinner.setVisibility(View.INVISIBLE);
                    break;
            }
        });

        viewModel.getSortOrder().observe(this, sortOrder -> {
            switch (sortOrder){
                case POPULAR:
                case TOP_RATED:
                    viewModel.fetchMovieData(getString(R.string.api_key_v3), sortOrder);
                    break;
                case FAVORITE:
                    viewModel.loadFavoriteMovies();
                    break;
            }
        });

        viewModel.getMovieList().observe(this, movies -> {
            if (movies != null){
                adapter.setMovieData(movies);
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
        Log.v(getLocalClassName(), "Selected "+item.getTitle());
        switch (item.getItemId()){
            case R.id.sort_popular_mi:
                viewModel.setSortOrder(MainViewModel.SortOrder.POPULAR);
                return true;
            case R.id.sort_highest_rated_mi:
                viewModel.setSortOrder(MainViewModel.SortOrder.TOP_RATED);
                return true;
            case R.id.show_favorites_mi:
                viewModel.setSortOrder(MainViewModel.SortOrder.FAVORITE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovieData(){
        loadMovieData(MainViewModel.SortOrder.POPULAR);
    }

    private void loadMovieData(MainViewModel.SortOrder sortOrder) {
        viewModel.setSortOrder(sortOrder);
    }

    @Override
    public void onListItemClick(MovieEntry movie) {
        Intent intent = new Intent(this, DetailActivity.class)
                .putExtra(JsonUtils.TITLE, movie.getTitle())
                .putExtra(JsonUtils.MOVIE_ID, movie.getId())
                .putExtra(JsonUtils.POSTER_PATH, movie.getPosterUrl())
                .putExtra(JsonUtils.OVERVIEW, movie.getDescription())
                .putExtra(JsonUtils.VOTE_AVERAGE, movie.getUserRating())
                .putExtra(JsonUtils.RELEASE_DATE, movie.getReleaseDate());
        startActivity(intent);
    }

}
