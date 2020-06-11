package com.android.example.popularmovies.database

import android.annotation.SuppressLint
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.android.example.popularmovies.ui.PosterSizes
import com.android.example.popularmovies.utils.json.JsonUtils
import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "movies")
data class MovieEntry(
        @PrimaryKey val id: Int,
        val title: String,
        @ColumnInfo(name = JsonUtils.OVERVIEW)
        @Json(name = JsonUtils.OVERVIEW)
        val description: String,
        @ColumnInfo(name = JsonUtils.VOTE_AVERAGE)
        @Json(name = JsonUtils.VOTE_AVERAGE)
        val userRating: Double,
        @ColumnInfo(name = JsonUtils.POSTER_PATH)
        @Json(name = JsonUtils.POSTER_PATH)
        val posterUrl: String,
        @ColumnInfo(name = JsonUtils.RELEASE_DATE)
        @Json(name = JsonUtils.RELEASE_DATE)
        var releaseDate: Date?
) {
    @Ignore
    @SuppressLint("SimpleDateFormat")
    constructor(id: Int, title: String, description: String, userRating: Double, posterUrl: String,
                releaseDateString: String) : this(
            id,
            title,
            description,
            userRating,
            posterUrl,
            SimpleDateFormat(DATE_PATTERN).parse(releaseDateString)
    )

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as MovieEntry
        return id == that.id && that.userRating.compareTo(userRating) == 0 &&
                title == that.title &&
                description == that.description &&
                posterUrl == that.posterUrl &&
                releaseDate == that.releaseDate
    }

    override fun hashCode(): Int {
        return Objects.hash(id, title, description, userRating, posterUrl, releaseDate)
    }

    fun getPosterUri(size: PosterSizes): Uri {
        return if (size == PosterSizes.ORIGINAL) {
            Uri.parse(JsonUtils.BASE_POSTER_PATH + "original" + posterUrl)
        } else {
            Uri.parse(JsonUtils.BASE_POSTER_PATH + 'w' + size.size + posterUrl)
        }
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
    }
}