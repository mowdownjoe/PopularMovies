package com.android.example.popularmovies.utils.json;

import com.google.common.truth.Truth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.fail;


public class JsonUtilsTest {

    //TODO Create unit tests
    @Test
    public void parseMovieReviewsJson_oneEntry() {
        //GIVEN
        JSONArray testData = new JSONArray();

        String exampleAuthor = "Rob";
        String exampleContent = "Bad movie.";
        String exampleID = "55d3a610925141201600079c";
        String exampleUrl = "https://themoviedb.org/review/"+exampleID;

        HashMap<String, String> exampleMap = new HashMap<>();
        exampleMap.put("author", exampleAuthor);
        exampleMap.put("content", exampleContent);
        exampleMap.put("id", exampleID);
        exampleMap.put("url", exampleUrl);
        testData.put(new JSONObject(exampleMap));

        MovieReview expectedReview = new MovieReview(exampleAuthor, exampleContent, exampleID, exampleUrl);

        //WHEN
        List<MovieReview> exampleReviewList;
        try {
            exampleReviewList = JsonUtils.parseMovieReviewsJson(testData);
        } catch (JSONException | IOException e) {
            fail(e.getMessage());
            return;
        }

        //THEN
        //Truth.assertThat(exampleReviewList.get(0)).isEqualTo(expectedReview);
        //Above fails due to getting difference references, in spite of the following passing.
        Truth.assertThat(exampleReviewList.get(0).getAuthor()).contains(exampleAuthor);
        Truth.assertThat(exampleReviewList.get(0).getContent()).contains(exampleContent);
        Truth.assertThat(exampleReviewList.get(0).getId()).contains(exampleID);
        Truth.assertThat(exampleReviewList.get(0).getUrl()).contains(exampleUrl);
    }

    @Test
    public void parseMovieReviewsJson_Empty_ExpectEmpty(){
        //GIVEN
        JSONArray testData = new JSONArray();

        //WHEN
        List<MovieReview> exampleReviewList;
        try {
            exampleReviewList = JsonUtils.parseMovieReviewsJson(testData);
        } catch (JSONException | IOException e) {
            fail(e.getMessage());
            return;
        }

        Truth.assertThat(exampleReviewList).isEmpty();
    }

    @Test
    public void parseMovieReviewsJson_Null_ExpectError(){
        //GIVEN
        JSONArray testData = null;

        //WHEN
        List<MovieReview> exampleReviewList;
        try {
            exampleReviewList = JsonUtils.parseMovieReviewsJson(testData);
        } catch (JSONException | IOException e) {
            fail(e.getMessage());
            return;
        } catch (NullPointerException e){
            System.out.println("Expected NullPointerException caught.");
            return;
        }
    }

    @Test
    public void parseMovieTrailersJson() {
    }
}