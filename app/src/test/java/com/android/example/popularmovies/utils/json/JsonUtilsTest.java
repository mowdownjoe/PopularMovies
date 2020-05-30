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
        Truth.assertThat(exampleReviewList.get(0)).isEqualTo(expectedReview);
    }

    @Test
    public void parseMovieReviewsJson_Empty_ExpectEmpty(){
        //GIVEN
        JSONArray testData = new JSONArray();

        //WHEN
        List<MovieReview> exampleReviewList;
        try {
            exampleReviewList = JsonUtils.parseMovieReviewsJson(testData);
        } catch (JSONException | IOException e) { //THEN
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
    public void movieReviewParsingFlow_FightClubJSON_ExpectTwoEntries(){
        try {
            //GIVEN
            JSONObject testJSON = new JSONObject(
                    "{\"id\":550,\"page\":1,\"results\":[{\"author\":" +
                    "\"Goddard\",\"content\":\"Pretty awesome movie.  It shows what one crazy " +
                    "person can convince other crazy people to do.  Everyone needs something to " +
                    "believe in.  I recommend Jesus Christ, but they want Tyler Durden.\",\"id\":\"" +
                    "5b1c13b9c3a36848f2026384\",\"" +
                    "url\":\"https://www.themoviedb.org/review/5b1c13b9c3a36848f2026384\"}," +
                    "{\"author\":\"Brett Pascoe\",\"content\":\"In my top 5 of all time favourite " +
                    "movies. Great story line and a movie you can watch over and over again.\",\"" +
                    "id\":\"5b3e1ba1925141144c007f17\",\"url\":\"" +
                    "https://www.themoviedb.org/review/5b3e1ba1925141144c007f17\"}],\"total_pages\"" +
                    ":1,\"total_results\":2}"
            );

            //WHEN
            JSONArray array = JsonUtils.getMovieReviews(testJSON);
            if (array == null){
                fail("JSON Array is null.");
                return;
            }
            List<MovieReview> outputReviewList = JsonUtils.parseMovieReviewsJson(array);

            //THEN
            Truth.assertThat(outputReviewList).hasSize(2);
            Truth.assertThat(outputReviewList.get(0).getAuthor()).matches("Goddard");
            Truth.assertThat(outputReviewList.get(1).getAuthor()).startsWith("Brett");
        } catch (JSONException | IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void parseMovieTrailersJson_OneEntry() {
        //GIVEN
        JSONArray testArray = new JSONArray();

        String id = "5c9294240e0a267cd516835f";
        String iso_639 = "en";
        String iso_3166 = "US";
        String key = "BdJKm16Co6M";
        String name = "Fight Club | #TBT Trailer | 20th Century FOX";
        String site = "YouTube";
        int size = 1080;
        String type = "Trailer";

        MovieVideo expectedVideo = new MovieVideo(
                id,
                name,
                key,
                size,
                type,
                site,
                iso_639+'_'+iso_3166
        );

        JSONObject equivalentJSON;
        List<MovieVideo> exampleVideoList;
        try {
            equivalentJSON = new JSONObject("{\"id\":\"5c9294240e0a267cd516835f\",\"" +
                    "iso_639_1\":\"en\",\"iso_3166_1\":\"US\",\"key\":\"BdJKm16Co6M\",\"name\":\"" +
                    "Fight Club | #TBT Trailer | 20th Century FOX\",\"site\":\"YouTube\",\"" +
                    "size\":1080,\"type\":\"Trailer\"}");
            testArray.put(equivalentJSON);

            //WHEN
            exampleVideoList = JsonUtils.parseMovieTrailersJson(testArray);
        } catch (JSONException | IOException e) {
            fail(e.getMessage());
            return;
        }

        //THEN
        Truth.assertThat(exampleVideoList.get(0)).isEqualTo(expectedVideo);
    }

    @Test
    public void parseMovieVideosJson_Empty_ExpectEmpty(){
        //GIVEN
        JSONArray testData = new JSONArray();

        //WHEN
        List<MovieVideo> exampleReviewList;
        try {
            exampleReviewList = JsonUtils.parseMovieTrailersJson(testData);
        } catch (JSONException | IOException e) { //THEN
            fail(e.getMessage());
            return;
        }

        Truth.assertThat(exampleReviewList).isEmpty();
    }

    @Test
    public void movieTrailerParsingFlow_FightClubJSON_ExpectTwoEntries(){
        try {
            //GIVEN
            JSONObject testJSON = new JSONObject(
                    "{\"id\":550,\"results\":[{\"id\":\"5c9294240e0a267cd516835f\",\"iso_639_1\":\"" +
                            "en\",\"iso_3166_1\":\"US\",\"key\":\"BdJKm16Co6M\",\"name\":\"" +
                            "Fight Club | #TBT Trailer | 20th Century FOX\",\"site\":\"YouTube\",\"" +
                            "size\":1080,\"type\":\"Trailer\"},{\"id\":\"5e382d1b4ca676001453826d\"," +
                            "\"iso_639_1\":\"en\",\"iso_3166_1\":\"US\",\"key\":\"6JnN1DmbqoU\",\"" +
                            "name\":\"Fight Club - Theatrical Trailer Remastered in HD\",\"site\":" +
                            "\"YouTube\",\"size\":1080,\"type\":\"Trailer\"}]}"
            );

            //WHEN
            JSONArray array = JsonUtils.getMovieTrailers(testJSON);
            if (array == null){
                fail("JSON Array is null.");
                return;
            }
            List<MovieVideo> outputVideoList = JsonUtils.parseMovieTrailersJson(array);

            //THEN
            Truth.assertThat(outputVideoList).hasSize(2);
            Truth.assertThat(outputVideoList.get(0).getName()).contains("Fight Club");
            Truth.assertThat(outputVideoList.get(1).getName()).contains("Fight Club");
        } catch (JSONException | IOException e) {
            fail(e.getMessage());
        }
    }
}