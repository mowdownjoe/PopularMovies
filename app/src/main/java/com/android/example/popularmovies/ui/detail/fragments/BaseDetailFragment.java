package com.android.example.popularmovies.ui.detail.fragments;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.example.popularmovies.database.MovieEntry;
import com.android.example.popularmovies.utils.json.JsonUtils;

import java.util.Date;

public class BaseDetailFragment extends Fragment {

    @Nullable
    public MovieEntry getMovie() {
        Intent intent = requireActivity().getIntent();
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
