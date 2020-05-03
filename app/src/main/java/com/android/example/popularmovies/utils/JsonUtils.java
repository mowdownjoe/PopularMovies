package com.android.example.popularmovies.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.example.popularmovies.DetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    private static final String RESULTS = "results";
    public static final String TITLE = "title";
    private static final String ERROR_STATUS = "status_code";
    private static final String ERROR_MESSAGE = "status_message";
    public static final String POSTER_PATH = "poster_path";
    public static final String OVERVIEW = "overview";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String RELEASE_DATE = "release_date";
    public static final String BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w500";
    public static final String BASE_THUMBNAIL_PATH = "https://image.tmdb.org/t/p/w185";

    @Nullable
    public static JSONArray getResultsArray(JSONObject rawResponse) throws JSONException, MovieJsonException {
        if (rawResponse.has(RESULTS)) {
            return rawResponse.getJSONArray(RESULTS);
        } else if (rawResponse.has(ERROR_STATUS)) {
            int errorCode = rawResponse.getInt(ERROR_STATUS);
            String errorMessage = rawResponse.getString(ERROR_MESSAGE);
            Log.e("JsonUtils.getResults", "HTTP error " + errorCode + ": " + errorMessage);
            return null;
        } else {
            throw new MovieJsonException("Unusual error detected.");
        }
    }

    @NonNull
    private static Uri getPosterUri(JSONObject movieData, boolean isThumbnail)
            throws JSONException, MovieJsonException {
        Uri result;
        if (movieData.has(POSTER_PATH)) {
            Uri uri;
            if (isThumbnail) {
                uri = Uri.parse(BASE_THUMBNAIL_PATH + movieData.getString(POSTER_PATH));
            } else {
                uri = Uri.parse(BASE_POSTER_PATH + movieData.getString(POSTER_PATH));
            }
            result = uri;
        } else {
            throw new MovieJsonException("Movie data is missing a poster path.");
        }
        return result;
    }

    @NonNull
    public static Uri getPosterUri(JSONObject movieData) throws JSONException, MovieJsonException {
        return getPosterUri(movieData, false);
    }


    @NonNull
    public static Intent buildMovieIntent(JSONObject movieData, Context context)
            throws JSONException, MovieJsonException {
        Intent intent = new Intent(context, DetailActivity.class);
        if (movieData.has(TITLE)) {
            String movieTitle = movieData.getString(TITLE);
            String posterUrl = getPosterUri(movieData, true).toString();
            String plotSynopsis = movieData.getString(OVERVIEW);
            double userRating = movieData.getDouble(VOTE_AVERAGE);
            String releaseDate = movieData.getString(RELEASE_DATE);
            intent.putExtra(TITLE, movieTitle)
                    .putExtra(POSTER_PATH, posterUrl)
                    .putExtra(OVERVIEW, plotSynopsis)
                    .putExtra(VOTE_AVERAGE, userRating)
                    .putExtra(RELEASE_DATE, releaseDate);
            return intent;
        } else {
            throw new MovieJsonException("Movie does not have a title.");
        }
    }
}
