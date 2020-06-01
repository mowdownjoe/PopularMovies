package com.android.example.popularmovies.utils.network;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android.example.popularmovies.utils.json.MovieReview;
import com.android.example.popularmovies.utils.json.MovieVideo;
import com.google.common.truth.Truth;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class NetworkUtilsTest {

    MockWebServer testServerPlsIgnore;

    @Before
    public void setUp() {
        testServerPlsIgnore = new MockWebServer();
    }

    @After
    public void tearDown() throws Exception {
        testServerPlsIgnore.shutdown();
    }

    @Test
    public void getMovies_errorResponse_expectError() {
        //GIVEN
        MockResponse response = new MockResponse().setResponseCode(408);
        testServerPlsIgnore.enqueue(response);

        //WHEN
        JSONObject jsonResponse;
        try {
            jsonResponse = NetworkUtils.getMovies(NetworkUtils.NOW_PLAYING_NODE, "not-the-real-key");
        } catch (IOException | JSONException | OkHttpException e) { //THEN
            System.out.println("Error expected and caught");
            return;
        }

       fail("Error not caught.");
    }

    @Test
    public void getMovieVideos_errorResponse_expectNull() {
        //GIVEN
        MockResponse response = new MockResponse().setResponseCode(408);
        testServerPlsIgnore.enqueue(response);

        //WHEN
        List<MovieVideo> jsonResponse;
        jsonResponse = NetworkUtils.getMovieVideos(42, "not-the-real-key");

        //THEN
        Truth.assertThat(jsonResponse).isNull();
    }

    @Test
    public void getMovieReviews_errorResponse_expectNull() {
        //GIVEN
        MockResponse response = new MockResponse().setResponseCode(408);
        testServerPlsIgnore.enqueue(response);

        //WHEN
        List<MovieReview> jsonResponse;
        jsonResponse = NetworkUtils.getMovieReviews(42, "not-the-real-key");

        //THEN
        Truth.assertThat(jsonResponse).isNull();
    }
}