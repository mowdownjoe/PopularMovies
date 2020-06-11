package com.android.example.popularmovies.utils.network

import com.android.example.popularmovies.database.MovieEntry
import com.squareup.moshi.Json

data class MovieResults (val page: Int,
                         @Json(name = "total_pages")
                         val totalPages: Int,
                         @Json(name = "total_results")
                         val totalResults: Int,
                         val results: List<MovieEntry>
)