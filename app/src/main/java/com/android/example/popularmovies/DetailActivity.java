package com.android.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.example.popularmovies.databinding.ActivityDetailBinding;
import com.android.example.popularmovies.utils.JsonUtils;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent.hasExtra(JsonUtils.TITLE)){
            binding.tvMovieTitle.setText(intent.getStringExtra(JsonUtils.TITLE));
            //Assuming that if one extra is included from explicit intent, others will be.
            binding.tvDesc.setText(intent.getStringExtra(JsonUtils.OVERVIEW));
            binding.tvReleaseDate.setText(intent.getStringExtra(JsonUtils.RELEASE_DATE));
            String rating = "User rating: "+ intent.getDoubleExtra(JsonUtils.VOTE_AVERAGE, 0.0);
            binding.tvRating.setText(rating);
            Picasso.get().load(intent.getStringExtra(JsonUtils.POSTER_PATH))
                    .placeholder(R.drawable.loading_poster)
                    .error(R.drawable.error_poster)
                    .into(binding.ivPosterThumbnail);
        }
    }
}
