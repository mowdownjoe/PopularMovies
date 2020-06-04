package com.android.example.popularmovies.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.example.popularmovies.R;
import com.android.example.popularmovies.database.MovieEntry;
import com.android.example.popularmovies.databinding.ActivityDetailBinding;
import com.android.example.popularmovies.utils.json.JsonUtils;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private DetailViewModel viewModel;
    private TabLayoutMediator mediator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (binding.tabs != null) {
            DetailSectionsPagerAdapter sectionsPagerAdapter = new DetailSectionsPagerAdapter(this);
            binding.viewPager.setAdapter(sectionsPagerAdapter); //Must exist in layout if tabs exist.
            mediator = new TabLayoutMediator(binding.tabs, binding.viewPager, ((tab, position) -> {
                switch (position) {
                    case 0:
                        tab.setText(R.string.tab_header_details);
                        break;
                    case 1:
                        tab.setText(R.string.tab_header_reviews);
                        break;
                    case 2:
                        tab.setText(R.string.tab_header_videos);
                        break;
                }
            }));
            mediator.attach();
        }

        MovieEntry movie = getMovie();
        if (movie != null) {
            viewModel = new ViewModelProvider(this,
                    DetailViewModelFactory.getInstance(getApplication(), movie))
                    .get(DetailViewModel.class);
        } else {
            finish();
            return;
        }

        viewModel.getIsFav().observe(this, isFav -> {
            if (isFav){
                binding.fab.setImageResource(R.drawable.ic_unfav_action);
            } else {
                binding.fab.setImageResource(R.drawable.ic_fav_action);
            }
        });

        binding.fab.setOnClickListener(view -> {
            try {
                boolean isFav = viewModel.getIsFav().getValue();
                if (isFav){
                    viewModel.removeFromFavs();
                } else {
                    viewModel.addToFavs();
                }
            } catch (NullPointerException e) {
                Log.e(getClass().getSimpleName(), "ViewModel has not initialized isFav.", e);
            }
        });
        binding.fab.setOnLongClickListener(v -> {
            try {
                boolean isFav = viewModel.getIsFav().getValue();
                if (isFav){
                    Toast.makeText(this, "Remove from Favorites", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Add to Favorites", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e) {
                Log.e(getClass().getSimpleName(), "ViewModel has not initialized isFav.", e);
                return false;
            }
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediator != null){
            mediator.detach();
            mediator = null;
        }
    }

    @Nullable
    public MovieEntry getMovie() {
        Intent intent = getIntent();
        if (intent.hasExtra(JsonUtils.TITLE)) {
            MovieEntry movie;
            movie = new MovieEntry(
                    intent.getIntExtra(JsonUtils.MOVIE_ID, -1),
                    intent.getStringExtra(JsonUtils.TITLE),
                    intent.getStringExtra(JsonUtils.OVERVIEW),
                    intent.getDoubleExtra(JsonUtils.VOTE_AVERAGE, 0.0),
                    intent.getStringExtra(JsonUtils.POSTER_PATH),
                    (Date) intent.getSerializableExtra(JsonUtils.RELEASE_DATE)
            );
            return movie;
        } else {
            return null;
        }
    }
}