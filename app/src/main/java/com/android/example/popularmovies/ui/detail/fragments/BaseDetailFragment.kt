package com.android.example.popularmovies.ui.detail.fragments

import androidx.fragment.app.Fragment
import com.android.example.popularmovies.database.MovieEntry
import com.android.example.popularmovies.utils.json.JsonUtils
import java.util.*

open class BaseDetailFragment : Fragment() {
    val movie: MovieEntry?
        get() {
            val intent = requireActivity().intent
            return if (intent.hasExtra(JsonUtils.TITLE)) {
                MovieEntry(
                        intent.getIntExtra(JsonUtils.MOVIE_ID, -1),
                        intent.getStringExtra(JsonUtils.TITLE),
                        intent.getStringExtra(JsonUtils.OVERVIEW),
                        intent.getDoubleExtra(JsonUtils.VOTE_AVERAGE, 0.0),
                        intent.getStringExtra(JsonUtils.POSTER_PATH),
                        intent.getSerializableExtra(JsonUtils.RELEASE_DATE) as Date
                )
            } else {
                null
            }
        }
}