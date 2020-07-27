package com.android.example.popularmovies.utils.network

import com.android.example.popularmovies.utils.json.MovieReview
import com.android.example.popularmovies.utils.json.MovieVideo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val ROOT_API_MOVIE_URL = "https://api.themoviedb.org/3/"
const val NOW_PLAYING_NODE = "/now_playing"
const val POPULAR_NODE = "/popular"
const val TOP_RATED_NODE = "/top_rated"
private const val API_KEY = "api_key"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder().run {
    addConverterFactory(MoshiConverterFactory.create(moshi))
    baseUrl(ROOT_API_MOVIE_URL)
    build()
}

interface MovieDbAPIService {

    @GET("movie/{sortOrder}")
    suspend fun getMovies(@Path("sortOrder") order: String,
                          @Query(API_KEY) apiKey: String,
                          @Query("page") page: Int = 1) : MovieResults

    @GET("movie/{movieId}/reviews")
    suspend fun getMovieReviews(@Path("movieId") movieId: Int, @Query(API_KEY) apiKey: String): List<MovieReview>

    @GET("movie/{movieId}/videos")
    suspend fun getMovieVideos(@Path("movieId") movieId: Int, @Query(API_KEY) apiKey: String): List<MovieVideo>
}

object MovieDbAPI {
    val apiService: MovieDbAPIService by lazy { retrofit.create(MovieDbAPIService::class.java) }
}