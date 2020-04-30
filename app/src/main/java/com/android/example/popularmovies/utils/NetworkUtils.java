package com.android.example.popularmovies.utils;

import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {

    private static final String TAG_GET_MOVIES = "NetworkUtils.getMovies";
    private static final String ROOT_API_URL = "https://api.themoviedb.org/3/movie";
    public static final String NOW_PLAYING_NODE = "/now_playing";
    public static final String POPULAR_NODE = "/popular";
    public static final String TOP_RATED_NODE = "/top_rated";
    private static final String API_KEY_QUERY = "?api_key=";

    @Nullable
    public static JSONObject getMovies(String node, String apiKey) throws IOException, JSONException {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(ROOT_API_URL+node+API_KEY_QUERY+apiKey)
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                return new JSONObject(response.body().string());
            } else {
                Log.w(TAG_GET_MOVIES, "Response from Movie Database is null.");
                return null;
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG_GET_MOVIES, "Error occurred when requesting movies now playing.", e);
            throw e;
        }
    }
}
