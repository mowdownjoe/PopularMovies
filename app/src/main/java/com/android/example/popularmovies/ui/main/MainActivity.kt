package com.android.example.popularmovies.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.android.example.popularmovies.R
import com.android.example.popularmovies.database.MovieEntry
import com.android.example.popularmovies.databinding.ActivityMainBinding
import com.android.example.popularmovies.ui.detail.DetailActivity
import com.android.example.popularmovies.ui.main.PosterAdapter.PosterOnClickListener
import com.android.example.popularmovies.utils.json.JsonUtils
import com.android.example.popularmovies.utils.network.LoadingStatus

class MainActivity : AppCompatActivity(), PosterOnClickListener {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var adapter: PosterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvMoviePosterGrid.setHasFixedSize(true)
        binding.rvMoviePosterGrid.layoutManager = GridAutofitLayoutManager(this, 490, RecyclerView.VERTICAL, false)
        adapter = PosterAdapter(this)
        binding.rvMoviePosterGrid.adapter = adapter
        viewModel.status.observe(this, Observer { loadingStatus: LoadingStatus ->
            when (loadingStatus) {
                LoadingStatus.LOADING -> {
                    binding.pbLoadingSpinner.visibility = View.VISIBLE
                    binding.rvMoviePosterGrid.visibility = View.INVISIBLE
                    binding.tvErrorText.visibility = View.INVISIBLE
                }
                LoadingStatus.DONE -> {
                    binding.rvMoviePosterGrid.visibility = View.VISIBLE
                    binding.tvErrorText.visibility = View.INVISIBLE
                    binding.pbLoadingSpinner.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    binding.tvErrorText.visibility = View.VISIBLE
                    binding.rvMoviePosterGrid.visibility = View.INVISIBLE
                    binding.pbLoadingSpinner.visibility = View.INVISIBLE
                }
            }
        })
        viewModel.sortOrder.observe(this, Observer { sortOrder: MainViewModel.SortOrder ->
            when (sortOrder) {
                MainViewModel.SortOrder.POPULAR,
                MainViewModel.SortOrder.TOP_RATED,
                MainViewModel.SortOrder.NOW_PLAYING ->
                    viewModel.fetchMovieData(getString(R.string.api_key_v3), sortOrder)
                MainViewModel.SortOrder.FAVORITE -> viewModel.loadFavoriteMovies()
            }
        })
        viewModel.movieList.observe(this, Observer { movies: List<MovieEntry>? ->
            if (movies != null) {
                adapter.setMovieData(movies)
            }
        })
        loadMovieData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sort_order_menu, menu)
        menu.findItem(R.id.sort_popular_mi).isChecked = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        Log.v(localClassName, "Selected " + item.title)
        when (item.itemId) {
            R.id.sort_popular_mi -> {
                viewModel.setSortOrder(MainViewModel.SortOrder.POPULAR)
                return true
            }
            R.id.sort_highest_rated_mi -> {
                viewModel.setSortOrder(MainViewModel.SortOrder.TOP_RATED)
                return true
            }
            R.id.show_favorites_mi -> {
                viewModel.setSortOrder(MainViewModel.SortOrder.FAVORITE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadMovieData(sortOrder: MainViewModel.SortOrder = MainViewModel.SortOrder.POPULAR) {
        viewModel.setSortOrder(sortOrder)
    }

    override fun onListItemClick(movie: MovieEntry) {
        val intent = Intent(this, DetailActivity::class.java)
                .putExtra(JsonUtils.TITLE, movie.title)
                .putExtra(JsonUtils.MOVIE_ID, movie.id)
                .putExtra(JsonUtils.POSTER_PATH, movie.posterUrl)
                .putExtra(JsonUtils.OVERVIEW, movie.description)
                .putExtra(JsonUtils.VOTE_AVERAGE, movie.userRating)
                .putExtra(JsonUtils.RELEASE_DATE, movie.releaseDate)
        startActivity(intent)
    }
}