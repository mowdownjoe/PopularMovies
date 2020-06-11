package com.android.example.popularmovies.utils.json

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.android.example.popularmovies.database.MovieEntry
import com.android.example.popularmovies.ui.PosterSizes
import com.android.example.popularmovies.ui.detail.DetailActivity
import com.android.example.popularmovies.utils.json.MovieVideo.VideoAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

object JsonUtils {
    private const val RESULTS = "results"
    const val TITLE = "title"
    private const val ERROR_STATUS = "status_code"
    private const val ERROR_MESSAGE = "status_message"
    const val MOVIE_ID = "id"
    const val POSTER_PATH = "poster_path"
    const val OVERVIEW = "overview"
    const val VOTE_AVERAGE = "vote_average"
    const val RELEASE_DATE = "release_date"
    const val BASE_POSTER_PATH = "https://image.tmdb.org/t/p/"
    const val REVIEWS = "reviews"
    const val VIDEOS = "videos"

    @kotlin.jvm.JvmStatic
    @Throws(JSONException::class)
    fun getMovieResultsArray(rawResponse: JSONObject): JSONArray? = if (rawResponse.has(RESULTS)) {
        rawResponse.getJSONArray(RESULTS)
    } else if (rawResponse.has(ERROR_STATUS)) {
        val errorCode = rawResponse.getInt(ERROR_STATUS)
        val errorMessage = rawResponse.getString(ERROR_MESSAGE)
        Log.e("JsonUtils.getResults", "HTTP error $errorCode: $errorMessage")
        null
    } else {
        throw MovieJsonException("Unusual error detected.")
    }

    @kotlin.jvm.JvmStatic
    @Throws(JSONException::class, IOException::class)
    fun parseMovieJson(moviesJson: JSONArray): List<MovieEntry?> {
        val moshi = Moshi.Builder()
                .add(Date::class.java, Rfc3339DateJsonAdapter())
                .build()
        val adapter = moshi.adapter(MovieEntry::class.java)
        val movies = ArrayList<MovieEntry?>()
        for (i in 0 until moviesJson.length()) {
            movies.add(adapter.fromJson(moviesJson.getJSONObject(i).toString()))
        }
        return movies
    }

    @kotlin.jvm.JvmStatic
    @Throws(JSONException::class)
    fun getMovieReviews(movieData: JSONObject): JSONArray? {
        if (movieData.has(REVIEWS)) {
            val reviewsObject = movieData.getJSONObject(REVIEWS)
            if (reviewsObject.has(RESULTS)) {
                return reviewsObject.getJSONArray(RESULTS)
            }
        } else if (movieData.has(RESULTS)) {
            return movieData.getJSONArray(RESULTS)
        } else if (movieData.has(ERROR_STATUS)) {
            val errorCode = movieData.getInt(ERROR_STATUS)
            val errorMessage = movieData.getString(ERROR_MESSAGE)
            Log.e("JsonUtils.getResults", "HTTP error $errorCode: $errorMessage")
            return null
        }
        throw MovieJsonException("No reviews found for movie.")
    }

    @kotlin.jvm.JvmStatic
    @Throws(JSONException::class, IOException::class)
    fun parseMovieReviewsJson(reviewJson: JSONArray?): List<MovieReview?> {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(MovieReview::class.java)
        val reviews = LinkedList<MovieReview?>()
        for (i in 0 until reviewJson!!.length()) {
            reviews.add(adapter.fromJson(reviewJson.getJSONObject(i).toString()))
        }
        return reviews
    }

    @kotlin.jvm.JvmStatic
    @Throws(JSONException::class)
    fun getMovieTrailers(movieData: JSONObject): JSONArray? {
        if (movieData.has(VIDEOS)) {
            val videoObject = movieData.getJSONObject(VIDEOS)
            if (videoObject.has(RESULTS)) {
                return videoObject.getJSONArray(VIDEOS)
            }
        } else if (movieData.has(RESULTS)) {
            return movieData.getJSONArray(RESULTS)
        } else if (movieData.has(ERROR_STATUS)) {
            val errorCode = movieData.getInt(ERROR_STATUS)
            val errorMessage = movieData.getString(ERROR_MESSAGE)
            Log.e("JsonUtils.getResults", "HTTP error $errorCode: $errorMessage")
            return null
        }
        throw MovieJsonException("No videos found for movie.")
    }

    @kotlin.jvm.JvmStatic
    @Throws(JSONException::class, IOException::class)
    fun parseMovieTrailersJson(videoJson: JSONArray?): List<MovieVideo?> {
        val moshi = Moshi.Builder().add(VideoAdapter()).build()
        val adapter = moshi.adapter(MovieVideo::class.java)
        val videos = LinkedList<MovieVideo?>()
        for (i in 0 until videoJson!!.length()) {
            videos.add(adapter.fromJson(videoJson.getJSONObject(i).toString()))
        }
        return videos
    }

    @Throws(JSONException::class)
    fun getPosterUri(movieData: JSONObject, size: PosterSizes): Uri =
            if (movieData.has(POSTER_PATH)) {
                if (size != PosterSizes.ORIGINAL) {
                    Uri.parse(BASE_POSTER_PATH + 'w' + size.size + movieData.getString(POSTER_PATH))
                } else {
                    Uri.parse(BASE_POSTER_PATH + "original" + movieData.getString(POSTER_PATH))
                }
            } else {
                throw MovieJsonException("Movie data is missing a poster path.")
            }

    @Throws(JSONException::class)
    private fun getPosterUri(movieData: JSONObject, isThumbnail: Boolean): Uri {
        return if (isThumbnail) {
            getPosterUri(movieData, PosterSizes.SMALL)
        } else {
            getPosterUri(movieData, PosterSizes.LARGE)
        }
    }

    @Throws(JSONException::class)
    fun getPosterUri(movieData: JSONObject): Uri {
        return getPosterUri(movieData, false)
    }

    @Throws(JSONException::class, MovieJsonException::class)
    fun buildMovieIntent(movieData: JSONObject, context: Context): Intent {
        val intent = Intent(context, DetailActivity::class.java)
        return if (movieData.has(TITLE)) {
            val movieId = movieData.getInt(MOVIE_ID)
            val movieTitle = movieData.getString(TITLE)
            val posterUrl = getPosterUri(movieData, true).toString()
            val plotSynopsis = movieData.getString(OVERVIEW)
            val userRating = movieData.getDouble(VOTE_AVERAGE)
            val releaseDate = movieData.getString(RELEASE_DATE)
            intent.putExtra(TITLE, movieTitle)
                    .putExtra(MOVIE_ID, movieId)
                    .putExtra(POSTER_PATH, posterUrl)
                    .putExtra(OVERVIEW, plotSynopsis)
                    .putExtra(VOTE_AVERAGE, userRating)
                    .putExtra(RELEASE_DATE, releaseDate)
            intent
        } else {
            throw MovieJsonException("Movie does not have a title.")
        }
    }
}