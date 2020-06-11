package com.android.example.popularmovies.utils.json

import android.net.Uri
import androidx.core.net.toUri
import java.util.*

class MovieReview(var author: String, var content: String, var id: String, var url: String) {

    val urlAsUri: Uri
        get() = url.toUri()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as MovieReview
        return author == that.author && content == that.content && id == that.id && url == that.url
    }

    override fun hashCode(): Int {
        return Objects.hash(author, content, id, url)
    }

}