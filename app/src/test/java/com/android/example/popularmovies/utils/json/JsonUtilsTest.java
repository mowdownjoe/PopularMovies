package com.android.example.popularmovies.utils.json;


import com.android.example.popularmovies.database.MovieEntry;
import com.google.common.collect.Range;
import com.squareup.moshi.JsonDataException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;


public class JsonUtilsTest {

    @Test
    public void parseMovieReviewsJson_OneEntry() {
        //GIVEN
        JSONArray testData = new JSONArray();

        String exampleAuthor = "Rob";
        String exampleContent = "Bad movie.";
        String exampleID = "55d3a610925141201600079c";
        String exampleUrl = "https://themoviedb.org/review/" + exampleID;

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
        assertThat(exampleReviewList.get(0)).isEqualTo(expectedReview);
    }

    @Test
    public void parseMovieReviewsJson_Empty_ExpectEmpty() {
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

        assertThat(exampleReviewList).isEmpty();
    }

    @Test
    public void parseMovieReviewsJson_Null_ExpectError() {
        //GIVEN
        JSONArray testData = null;

        //WHEN
        List<MovieReview> exampleReviewList;
        try {
            exampleReviewList = JsonUtils.parseMovieReviewsJson(testData);
        } catch (JSONException | IOException e) {
            fail(e.getMessage());
            return;
        } catch (NullPointerException e) {
            System.out.println("Expected NullPointerException caught.");
            return;
        }
    }

    @Test
    public void movieReviewParsingFlow_FightClubJSON_ExpectTwoEntries() {
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
            if (array == null) {
                fail("JSON Array is null.");
                return;
            }
            List<MovieReview> outputReviewList = JsonUtils.parseMovieReviewsJson(array);

            //THEN
            assertThat(outputReviewList).hasSize(2);
            assertThat(outputReviewList.get(0).getAuthor()).matches("Goddard");
            assertThat(outputReviewList.get(1).getAuthor()).startsWith("Brett");
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
                iso_639 + '_' + iso_3166
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
        assertThat(exampleVideoList.get(0)).isEqualTo(expectedVideo);
        assertThat(exampleVideoList.get(0).getIsoIdentifier()).matches("en_US");
    }

    @Test
    public void parseMovieVideosJson_Empty_ExpectEmpty() {
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

        assertThat(exampleReviewList).isEmpty();
    }

    @Test
    public void movieTrailerParsingFlow_FightClubJSON_ExpectTwoEntries() {
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
            if (array == null) {
                fail("JSON Array is null.");
                return;
            }
            List<MovieVideo> outputVideoList = JsonUtils.parseMovieTrailersJson(array);

            //THEN
            assertThat(outputVideoList).hasSize(2);
            assertThat(outputVideoList.get(0).getName()).contains("Fight Club");
            assertThat(outputVideoList.get(1).getName()).contains("Fight Club");
        } catch (JSONException | IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void newMovieParsingFlow_expectTwentyEntries() {
        try {
            //GIVEN the following JSON
            String jsonString = "{\"page\":1,\"total_results\":10000,\"total_pages\":500,\"results\":[{\"popularity\":364.734,\"vote_count\":3557,\"video\":false,\"poster_path\":\"\\/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg\",\"id\":419704,\"adult\":false,\"backdrop_path\":\"\\/5BwqwxMEjeFtdknRV792Svo0K1v.jpg\",\"original_language\":\"en\",\"original_title\":\"Ad Astra\",\"genre_ids\":[18,878],\"title\":\"Ad Astra\",\"vote_average\":6,\"overview\":\"The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.\",\"release_date\":\"2019-09-17\"},{\"popularity\":152.335,\"vote_count\":2468,\"video\":false,\"poster_path\":\"\\/8WUVHemHFH2ZIP6NWkwlHWsyrEL.jpg\",\"id\":338762,\"adult\":false,\"backdrop_path\":\"\\/ocUrMYbdjknu2TwzMHKT9PBBQRw.jpg\",\"original_language\":\"en\",\"original_title\":\"Bloodshot\",\"genre_ids\":[28,878],\"title\":\"Bloodshot\",\"vote_average\":7,\"overview\":\"After he and his wife are murdered, marine Ray Garrison is resurrected by a team of scientists. Enhanced with nanotechnology, he becomes a superhuman, biotech killing machine—'Bloodshot'. As Ray first trains with fellow super-soldiers, he cannot recall anything from his former life. But when his memories flood back and he remembers the man that killed both him and his wife, he breaks out of the facility to get revenge, only to discover that there's more to the conspiracy than he thought.\",\"release_date\":\"2020-03-05\"},{\"popularity\":201.194,\"vote_count\":829,\"video\":false,\"poster_path\":\"\\/jHo2M1OiH9Re33jYtUQdfzPeUkx.jpg\",\"id\":385103,\"adult\":false,\"backdrop_path\":\"\\/fKtYXUhX5fxMxzQfyUcQW9Shik6.jpg\",\"original_language\":\"en\",\"original_title\":\"Scoob!\",\"genre_ids\":[12,16,35,9648,10751],\"title\":\"Scoob!\",\"vote_average\":8,\"overview\":\"In Scooby-Doo’s greatest adventure yet, see the never-before told story of how lifelong friends Scooby and Shaggy first met and how they joined forces with young detectives Fred, Velma, and Daphne to form the famous Mystery Inc. Now, with hundreds of cases solved, Scooby and the gang face their biggest, toughest mystery ever: an evil plot to unleash the ghost dog Cerberus upon the world. As they race to stop this global “dogpocalypse,” the gang discovers that Scooby has a secret legacy and an epic destiny greater than anyone ever imagined.\",\"release_date\":\"2020-05-15\"},{\"popularity\":170.1,\"vote_count\":80,\"video\":false,\"poster_path\":\"\\/niyXFhGIk4W2WTcX2Eod8vx2Mfe.jpg\",\"id\":686245,\"adult\":false,\"backdrop_path\":\"\\/pPguXG07MDRKH1agJdw1mWzuEkP.jpg\",\"original_language\":\"en\",\"original_title\":\"Survive the Night\",\"genre_ids\":[28,53],\"title\":\"Survive the Night\",\"vote_average\":5.8,\"overview\":\"A disgraced doctor and his family are held hostage at their home by criminals on the run, when a robbery-gone-awry requires them to seek immediate medical attention.\",\"release_date\":\"2020-05-22\"},{\"popularity\":116.175,\"vote_count\":4294,\"video\":false,\"poster_path\":\"\\/aQvJ5WPzZgYVDrxLX4R6cLJCEaQ.jpg\",\"id\":454626,\"adult\":false,\"backdrop_path\":\"\\/stmYfCUGd8Iy6kAMBr6AmWqx8Bq.jpg\",\"original_language\":\"en\",\"original_title\":\"Sonic the Hedgehog\",\"genre_ids\":[28,35,878,10751],\"title\":\"Sonic the Hedgehog\",\"vote_average\":7.5,\"overview\":\"Based on the global blockbuster videogame franchise from Sega, Sonic the Hedgehog tells the story of the world’s speediest hedgehog as he embraces his new home on Earth. In this live-action adventure comedy, Sonic and his new best friend team up to defend the planet from the evil genius Dr. Robotnik and his plans for world domination.\",\"release_date\":\"2020-02-12\"},{\"popularity\":121.626,\"vote_count\":4165,\"video\":false,\"poster_path\":\"\\/y95lQLnuNKdPAzw9F9Ab8kJ80c3.jpg\",\"id\":38700,\"adult\":false,\"backdrop_path\":\"\\/upUy2QhMZEmtypPW3PdieKLAHxh.jpg\",\"original_language\":\"en\",\"original_title\":\"Bad Boys for Life\",\"genre_ids\":[28,80,53],\"title\":\"Bad Boys for Life\",\"vote_average\":7.2,\"overview\":\"Marcus and Mike are forced to confront new threats, career changes, and midlife crises as they join the newly created elite team AMMO of the Miami police department to take down the ruthless Armando Armas, the vicious leader of a Miami drug cartel.\",\"release_date\":\"2020-01-15\"},{\"popularity\":98.639,\"vote_count\":4203,\"video\":false,\"poster_path\":\"\\/h4VB6m0RwcicVEZvzftYZyKXs6K.jpg\",\"id\":495764,\"adult\":false,\"backdrop_path\":\"\\/kvbbK2rLGSJh9rf6gg1i1iVLYQS.jpg\",\"original_language\":\"en\",\"original_title\":\"Birds of Prey (and the Fantabulous Emancipation of One Harley Quinn)\",\"genre_ids\":[28,35,80],\"title\":\"Birds of Prey (and the Fantabulous Emancipation of One Harley Quinn)\",\"vote_average\":7.2,\"overview\":\"Harley Quinn joins forces with a singer, an assassin and a police detective to help a young girl who had a hit placed on her after she stole a rare diamond from a crime lord.\",\"release_date\":\"2020-02-05\"},{\"popularity\":101.558,\"vote_count\":12837,\"video\":false,\"poster_path\":\"\\/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg\",\"id\":475557,\"adult\":false,\"backdrop_path\":\"\\/f5F4cRhQdUbyVbB5lTNCwUzD6BP.jpg\",\"original_language\":\"en\",\"original_title\":\"Joker\",\"genre_ids\":[80,18,53],\"title\":\"Joker\",\"vote_average\":8.2,\"overview\":\"During the 1980s, a failed stand-up comedian is driven insane and turns to a life of crime and chaos in Gotham City while becoming an infamous psychopathic crime figure.\",\"release_date\":\"2019-10-02\"},{\"popularity\":99.306,\"vote_count\":12619,\"video\":false,\"poster_path\":\"\\/tQf4DUygWo64AOuqgk4jEDCE3Ws.jpg\",\"id\":210577,\"adult\":false,\"backdrop_path\":\"\\/5Tqdk3eezqu945KcBtz3SYQxidN.jpg\",\"original_language\":\"en\",\"original_title\":\"Gone Girl\",\"genre_ids\":[18,9648,53],\"title\":\"Gone Girl\",\"vote_average\":7.9,\"overview\":\"With his wife's disappearance having become the focus of an intense media circus, a man sees the spotlight turned on him when it's suspected that he may not be innocent.\",\"release_date\":\"2014-10-01\"},{\"popularity\":85.958,\"vote_count\":5178,\"video\":false,\"poster_path\":\"\\/iZf0KyrE25z1sage4SYFLCCrMi9.jpg\",\"id\":530915,\"adult\":false,\"backdrop_path\":\"\\/cqa3sa4c4jevgnEJwq3CMF8UfTG.jpg\",\"original_language\":\"en\",\"original_title\":\"1917\",\"genre_ids\":[28,18,36,10752],\"title\":\"1917\",\"vote_average\":7.9,\"overview\":\"At the height of the First World War, two young British soldiers must cross enemy territory and deliver a message that will stop a deadly attack on hundreds of soldiers.\",\"release_date\":\"2019-12-25\"},{\"popularity\":92.107,\"vote_count\":29,\"video\":false,\"poster_path\":\"\\/m2rJGjlesDKxugl7ypW8n3Mipjl.jpg\",\"id\":620883,\"adult\":false,\"backdrop_path\":\"\\/prnq2ONhqo9Tga7dOMZKgFJMofs.jpg\",\"original_language\":\"es\",\"original_title\":\"La corazonada\",\"genre_ids\":[80,53],\"title\":\"Intuition\",\"vote_average\":5.7,\"overview\":\"Police officer Pipa works on her first big case while simultaneously investigating her boss, who is suspected of murder. The prequel to \\\"Perdida\\\".\",\"release_date\":\"2020-05-28\"},{\"popularity\":126.482,\"vote_count\":732,\"video\":false,\"poster_path\":\"\\/wxPhn4ef1EAo5njxwBkAEVrlJJG.jpg\",\"id\":514847,\"adult\":false,\"backdrop_path\":\"\\/naXUDz0VGK7aaPlEpsuYW8kNVsr.jpg\",\"original_language\":\"en\",\"original_title\":\"The Hunt\",\"genre_ids\":[28,27,53],\"title\":\"The Hunt\",\"vote_average\":6.7,\"overview\":\"Twelve strangers wake up in a clearing. They don't know where they are—or how they got there. In the shadow of a dark internet conspiracy theory, ruthless elitists gather at a remote location to hunt humans for sport. But their master plan is about to be derailed when one of the hunted turns the tables on her pursuers.\",\"release_date\":\"2020-03-11\"},{\"popularity\":122.766,\"vote_count\":1268,\"video\":false,\"poster_path\":\"\\/33VdppGbeNxICrFUtW2WpGHvfYc.jpg\",\"id\":481848,\"adult\":false,\"backdrop_path\":\"\\/9sXHqZTet3Zg5tgcc0hCDo8Tn35.jpg\",\"original_language\":\"en\",\"original_title\":\"The Call of the Wild\",\"genre_ids\":[12,18,10751],\"title\":\"The Call of the Wild\",\"vote_average\":7.4,\"overview\":\"Buck is a big-hearted dog whose blissful domestic life is turned upside down when he is suddenly uprooted from his California home and transplanted to the exotic wilds of the Yukon during the Gold Rush of the 1890s. As the newest rookie on a mail delivery dog sled team—and later its leader—Buck experiences the adventure of a lifetime, ultimately finding his true place in the world and becoming his own master.\",\"release_date\":\"2020-02-19\"},{\"popularity\":73.326,\"vote_count\":2016,\"video\":false,\"poster_path\":\"\\/f4aul3FyD3jv3v4bul1IrkWZvzq.jpg\",\"id\":508439,\"adult\":false,\"backdrop_path\":\"\\/xFxk4vnirOtUxpOEWgA1MCRfy6J.jpg\",\"original_language\":\"en\",\"original_title\":\"Onward\",\"genre_ids\":[12,16,35,14,10751],\"title\":\"Onward\",\"vote_average\":7.9,\"overview\":\"In a suburban fantasy world, two teenage elf brothers embark on an extraordinary quest to discover if there is still a little magic left out there.\",\"release_date\":\"2020-02-29\"},{\"popularity\":84.822,\"vote_count\":7660,\"video\":false,\"poster_path\":\"\\/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg\",\"id\":496243,\"adult\":false,\"backdrop_path\":\"\\/ApiBzeaa95TNYliSbQ8pJv4Fje7.jpg\",\"original_language\":\"ko\",\"original_title\":\"기생충\",\"genre_ids\":[35,18,53],\"title\":\"Parasite\",\"vote_average\":8.5,\"overview\":\"All unemployed, Ki-taek's family takes peculiar interest in the wealthy and glamorous Parks for their livelihood until they get entangled in an unexpected incident.\",\"release_date\":\"2019-05-30\"},{\"popularity\":87.405,\"vote_count\":9419,\"video\":false,\"poster_path\":\"\\/qa6HCwP4Z15l3hpsASz3auugEW6.jpg\",\"id\":920,\"adult\":false,\"backdrop_path\":\"\\/sd4xN5xi8tKRPrJOWwNiZEile7f.jpg\",\"original_language\":\"en\",\"original_title\":\"Cars\",\"genre_ids\":[12,16,35,10751],\"title\":\"Cars\",\"vote_average\":6.8,\"overview\":\"Lightning McQueen, a hotshot rookie race car driven to succeed, discovers that life is about the journey, not the finish line, when he finds himself unexpectedly detoured in the sleepy Route 66 town of Radiator Springs. On route across the country to the big Piston Cup Championship in California to compete against two seasoned pros, McQueen gets to know the town's offbeat characters.\",\"release_date\":\"2006-06-08\"},{\"popularity\":78.894,\"vote_count\":5968,\"video\":false,\"poster_path\":\"\\/3iYQTLGoy7QnjcUYRJy4YrAgGvp.jpg\",\"id\":420817,\"adult\":false,\"backdrop_path\":\"\\/v4yVTbbl8dE1UP2dWu5CLyaXOku.jpg\",\"original_language\":\"en\",\"original_title\":\"Aladdin\",\"genre_ids\":[12,35,14,10749,10751],\"title\":\"Aladdin\",\"vote_average\":7.1,\"overview\":\"A kindhearted street urchin named Aladdin embarks on a magical adventure after finding a lamp that releases a wisecracking genie while a power-hungry Grand Vizier vies for the same lamp that has the power to make their deepest wishes come true.\",\"release_date\":\"2019-05-22\"},{\"popularity\":79.204,\"vote_count\":165,\"video\":false,\"poster_path\":\"\\/5jdLnvALCpK1NkeQU1z4YvOe2dZ.jpg\",\"id\":576156,\"adult\":false,\"backdrop_path\":\"\\/1EGFjibWzsN2GNNeOSQBYhQ9pK5.jpg\",\"original_language\":\"en\",\"original_title\":\"The Lovebirds\",\"genre_ids\":[28,35,10749],\"title\":\"The Lovebirds\",\"vote_average\":6.4,\"overview\":\"A couple experiences a defining moment in their relationship when they are unintentionally embroiled in a murder mystery. As their journey to clear their names takes them from one extreme – and hilarious - circumstance to the next, they must figure out how they, and their relationship, can survive the night.\",\"release_date\":\"2020-05-22\"},{\"popularity\":80.029,\"vote_count\":2233,\"video\":false,\"poster_path\":\"\\/wlfDxbGEsW58vGhFljKkcR5IxDj.jpg\",\"id\":545609,\"adult\":false,\"backdrop_path\":\"\\/1R6cvRtZgsYCkh8UFuWFN33xBP4.jpg\",\"original_language\":\"en\",\"original_title\":\"Extraction\",\"genre_ids\":[28,18,53],\"title\":\"Extraction\",\"vote_average\":7.5,\"overview\":\"Tyler Rake, a fearless mercenary who offers his services on the black market, embarks on a dangerous mission when he is hired to rescue the kidnapped son of a Mumbai crime lord…\",\"release_date\":\"2020-04-24\"},{\"popularity\":68.87,\"vote_count\":4719,\"video\":false,\"poster_path\":\"\\/pjeMs3yqRmFL3giJy4PMXWZTTPa.jpg\",\"id\":330457,\"adult\":false,\"backdrop_path\":\"\\/xJWPZIYOEFIjZpBL7SVBGnzRYXp.jpg\",\"original_language\":\"en\",\"original_title\":\"Frozen II\",\"genre_ids\":[12,16,10751],\"title\":\"Frozen II\",\"vote_average\":7.2,\"overview\":\"Elsa, Anna, Kristoff and Olaf head far into the forest to learn the truth about an ancient mystery of their kingdom.\",\"release_date\":\"2019-11-20\"}]}";
            JSONObject movieJson = new JSONObject(jsonString);

            //And GIVEN the following dates
            GregorianCalendar cal1 = new GregorianCalendar(2019, Calendar.SEPTEMBER, 16);
            GregorianCalendar cal2 = new GregorianCalendar(2019, Calendar.SEPTEMBER, 18);

            //WHEN
            JSONArray array = JsonUtils.getMovieResultsArray(movieJson);
            if (array == null) {
                fail("JSON Array is null.");
                return;
            }
            List<MovieEntry> outputList = JsonUtils.parseMovieJson(array);

            //THEN
            assertThat(outputList).hasSize(20);
            assertThat(outputList.get(0).getTitle()).matches("Ad Astra");
            assertThat(outputList.get(0).getReleaseDate()).isIn(Range.closed(cal1.getTime(), cal2.getTime()));
            assertThat(outputList.get(1).getTitle()).matches("Bloodshot");

        } catch (JSONException | IOException | JsonDataException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}