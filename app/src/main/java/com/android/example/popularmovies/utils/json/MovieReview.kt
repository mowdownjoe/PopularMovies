package com.android.example.popularmovies.utils.json

import android.net.Uri
import androidx.core.net.toUri
import java.util.*

class MovieReview(val author: String, val content: String, val id: String, val url: String) {

    val urlAsUri: Uri
        get() = url.toUri()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as MovieReview
        return author == that.author && content == that.content && id == that.id && url == that.url
    }

    override fun hashCode(): Int {
        return Objects.hash(author, content, id, url)
    }

}