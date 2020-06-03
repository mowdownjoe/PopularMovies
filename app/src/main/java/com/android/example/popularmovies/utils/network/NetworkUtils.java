package com.android.example.popularmovies.utils.network;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.android.example.popularmovies.utils.json.JsonUtils;
import com.android.example.popularmovies.utils.json.MovieReview;
import com.android.example.popularmovies.utils.json.MovieVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {

    private static final String TAG_GET_MOVIES = "NetworkUtils.getMovies";
    private static final String TAG_GET_REVIEWS = "NetworkUtils.getReviews";
    private static final String TAG_GET_VIDEOS = "NetworkUtils.getVideos";
    private static final String ROOT_API_MOVIE_URL = "https://api.themoviedb.org/3/movie";
    public static final String NOW_PLAYING_NODE = "/now_playing";
    public static final String POPULAR_NODE = "/popular";
    public static final String TOP_RATED_NODE = "/top_rated";
    private static final String API_KEY_QUERY = "?api_key=";

    @Nullable
    @WorkerThread
    public static JSONObject getMovies(String node, String apiKey) throws IOException, JSONException {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(ROOT_API_MOVIE_URL +node+API_KEY_QUERY+apiKey)
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200 && response.body() != null) {
                return new JSONObject(response.body().string());
            } else if (response.code() >= 400) {
                throw new OkHttpException(response);
            } else {
                Log.w(TAG_GET_MOVIES, "Response from Movie Database is null.");
                return null;
            }
        } catch (IOException | JSONException | OkHttpException e) {
            Log.e(TAG_GET_MOVIES, "Error occurred when requesting movies from node "+node, e);
            throw e;
        }
    }

    @WorkerThread
    @Nullable
    public static List<MovieVideo> getMovieVideos(int movieId, String apiKey)  {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(ROOT_API_MOVIE_URL +'/'+movieId+"/videos"+API_KEY_QUERY+apiKey)
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200 && response.body() != null){
                JSONArray videosJson = JsonUtils.getMovieTrailers(new JSONObject(response.body().string()));
                return JsonUtils.parseMovieTrailersJson(videosJson);
            } else if (response.code() >= 400) {
                throw new OkHttpException(response);
            } else {
                return null;
            }
        } catch (IOException | JSONException | OkHttpException e) {
            Log.e(TAG_GET_VIDEOS, "Error occurred while requesting videos for movie", e);
            return null;
        }
    }

    @WorkerThread
    @Nullable
    public static List<MovieReview> getMovieReviews(int movieId, String apiKey){
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(ROOT_API_MOVIE_URL +'/'+movieId+"/reviews"+API_KEY_QUERY+apiKey)
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() == 200 && response.body() != null){
                JSONArray reviewsJson = JsonUtils.getMovieReviews(new JSONObject(response.body().string()));
                return JsonUtils.parseMovieReviewsJson(reviewsJson);
            } else if (response.code() >= 400) {
                throw new OkHttpException(response);
            } else {
                return null;
            }
        } catch (IOException | JSONException | OkHttpException e) {
            Log.e(TAG_GET_REVIEWS, "Error occurred while requesting reviews for movie", e);
            return null;
        }
    }
}
