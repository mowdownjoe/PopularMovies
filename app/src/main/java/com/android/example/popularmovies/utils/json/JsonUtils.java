package com.android.example.popularmovies.utils.json;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.example.popularmovies.ui.detail.DetailActivity;
import com.android.example.popularmovies.utils.MovieJsonException;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class JsonUtils {

    private static final String RESULTS = "results";
    public static final String TITLE = "title";
    private static final String ERROR_STATUS = "status_code";
    private static final String ERROR_MESSAGE = "status_message";
    public static final String MOVIE_ID = "id";
    public static final String POSTER_PATH = "poster_path";
    public static final String OVERVIEW = "overview";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String RELEASE_DATE = "release_date";
    private static final String BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w500";
    private static final String BASE_THUMBNAIL_PATH = "https://image.tmdb.org/t/p/w185";
    public static final String REVIEWS = "reviews";
    public static final String VIDEOS = "videos";

    @Nullable
    public static JSONArray getResultsArray(@NonNull JSONObject rawResponse) throws JSONException {
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

    public static JSONArray getMovieReviews(@NonNull JSONObject movieData) throws JSONException {
        if (movieData.has(REVIEWS)){
            JSONObject reviewsObject = movieData.getJSONObject(REVIEWS);
            if (reviewsObject.has(RESULTS)){
                return reviewsObject.getJSONArray(RESULTS);
            }
        }
        throw new MovieJsonException("No reviews found for movie.");
    }

    public static List<MovieReview> getMovieReviews(JSONArray reviewJson) throws JSONException, IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<MovieReview> adapter = moshi.adapter(MovieReview.class);
        LinkedList<MovieReview> reviews = new LinkedList<>();
        for (int i = 0; i < reviewJson.length(); i++) {
            reviews.add(adapter.fromJson(reviewJson.getJSONObject(i).toString()));
        }
        return reviews;
    }

    public static JSONArray getMovieTrailers(@NonNull JSONObject movieData) throws JSONException {
        if (movieData.has(VIDEOS)){
            JSONObject videoObject = movieData.getJSONObject(VIDEOS);
            if (videoObject.has(RESULTS)){
                return videoObject.getJSONArray(VIDEOS);
            }
        }
        throw new MovieJsonException("No videos found for movie.");
    }

    public static List<MovieVideo> getMovieTrailers(JSONArray videoJson) throws JSONException, IOException {
        Moshi moshi = new Moshi.Builder().add(new MovieVideo.VideoAdapter()).build();
        JsonAdapter<MovieVideo> adapter = moshi.adapter(MovieVideo.class);
        LinkedList<MovieVideo> videos = new LinkedList<>();
        for (int i = 0; i < videoJson.length(); i++) {
            videos.add(adapter.fromJson(videoJson.getJSONObject(i).toString()));
        }
        return videos;
    }

    @NonNull
    private static Uri getPosterUri(@NonNull JSONObject movieData, boolean isThumbnail)
            throws JSONException {
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
    public static Uri getPosterUri(@NonNull JSONObject movieData) throws JSONException {
        return getPosterUri(movieData, false);
    }


    @NonNull
    public static Intent buildMovieIntent(@NonNull JSONObject movieData, @NonNull Context context)
            throws JSONException, MovieJsonException {
        Intent intent = new Intent(context, DetailActivity.class);
        if (movieData.has(TITLE)) {
            int movieId = movieData.getInt(MOVIE_ID);
            String movieTitle = movieData.getString(TITLE);
            String posterUrl = getPosterUri(movieData, true).toString();
            String plotSynopsis = movieData.getString(OVERVIEW);
            double userRating = movieData.getDouble(VOTE_AVERAGE);
            String releaseDate = movieData.getString(RELEASE_DATE);
            intent.putExtra(TITLE, movieTitle)
                    .putExtra(MOVIE_ID, movieId)
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
