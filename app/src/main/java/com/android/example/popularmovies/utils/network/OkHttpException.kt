package com.android.example.popularmovies.utils.network

import okhttp3.Response

class OkHttpException(private val response: Response) : RuntimeException() {
    override val message: String?
        get() = when (response.code) {
            400 -> "Invalid request syntax"
            401 -> "Not authorized to access this information"
            404 -> "The server could not be found"
            else -> super.message
        }

}