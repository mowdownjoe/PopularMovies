package com.android.example.popularmovies.ui.detail.fragments;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.example.popularmovies.database.FavMovieEntry;
import com.android.example.popularmovies.utils.json.JsonUtils;

import java.text.ParseException;

public class BaseDetailFragment extends Fragment {

    @Nullable
    public FavMovieEntry getMovie() {
        Intent intent = requireActivity().getIntent();
        if (intent.hasExtra(JsonUtils.TITLE)) {
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
                return movie;
            } catch (ParseException e) {
                Log.e(getClass().getSimpleName(), "Could not parse date passed by API", e);
                requireActivity().finish();
                return null;
            }
        } else {
            return null;
        }
    }
}
