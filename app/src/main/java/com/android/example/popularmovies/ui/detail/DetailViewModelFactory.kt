package com.android.example.popularmovies.ui.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.android.example.popularmovies.database.MovieEntry

class DetailViewModelFactory private constructor(private val application: Application, private val mMovieEntry: MovieEntry) : AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(application, mMovieEntry) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        private val LOCK = Any()
        private var sInstance: DetailViewModelFactory? = null
        fun getInstance(app: Application?): DetailViewModelFactory? {
            requireNotNull(sInstance) { "Instance not created and movie not passed." }
            return sInstance
        }

        fun getInstance(app: Application, movieEntry: MovieEntry): DetailViewModelFactory? {
            if (sInstance == null || sInstance!!.mMovieEntry != movieEntry) {
                synchronized(LOCK) { sInstance = DetailViewModelFactory(app, movieEntry) }
            }
            return sInstance
        }
    }

}