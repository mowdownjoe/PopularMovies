package com.android.example.popularmovies.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.example.popularmovies.R;
import com.android.example.popularmovies.database.FavMovieEntry;
import com.android.example.popularmovies.databinding.ActivityDetailBinding;
import com.android.example.popularmovies.utils.JsonUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;
    DetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent.hasExtra(JsonUtils.TITLE)){
            FavMovieEntry movie;
            try {
                movie = new FavMovieEntry(
                        intent.getIntExtra(JsonUtils.MOVIE_ID, -1),
                        intent.getStringExtra(JsonUtils.TITLE),
                        intent.getStringExtra(JsonUtils.OVERVIEW),
                        intent.getDoubleExtra(JsonUtils.VOTE_AVERAGE, 0.0),
                        intent.getStringExtra(JsonUtils.POSTER_PATH),
                        intent.getStringExtra(JsonUtils.RELEASE_DATE)
                );
            } catch (ParseException e) {
                Log.e(getClass().getSimpleName(), "Could not parse date passed by API", e);
                finish();
                return;
            }

            viewModel = new ViewModelProvider(this, new DetailViewModelFactory(getApplication(), movie))
                    .get(DetailViewModel.class);

            binding.tvMovieTitle.setText(movie.getTitle());
            //Assuming that if one extra is included from explicit intent, others will be.
            binding.tvDesc.setText(movie.getDescription());
            binding.tvReleaseDate.setText(getString(R.string.date_detail, movie.getReleaseDate()));
            binding.tvRating.setText(getString(R.string.rating_detail, movie.getUserRating()));
            Picasso.get().load(movie.getPosterUrl())
                    .placeholder(R.drawable.loading_poster)
                    .error(R.drawable.error_poster)
                    .into(binding.ivPosterThumbnail);
        } else {
            finish();
        }
    }
}
