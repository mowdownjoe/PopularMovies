package com.android.example.popularmovies.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.example.popularmovies.database.FavMovieDatabase
import com.android.example.popularmovies.database.MovieEntry
import kotlinx.coroutines.launch

class DetailViewModel(private val app: Application, movie: MovieEntry) : AndroidViewModel(app) {
    private val _movie: MutableLiveData<MovieEntry> = MutableLiveData(movie)
    val movie: LiveData<MovieEntry>
        get() = _movie
    private val _isFav: MutableLiveData<Boolean> = MutableLiveData()
    val isFav: LiveData<Boolean>
        get() = _isFav

    private val isMovieFavorite: Boolean
        get() {
            val id = requireNotNull(movie.value).id
            return FavMovieDatabase.getInstance(app)!!.favMovieDao()
                    .countMovieById(id) > 0
        }

    private fun initializeIsFav() {
        viewModelScope.launch {
            val id = requireNotNull(movie.value).id
            _isFav.postValue(requireNotNull(FavMovieDatabase.getInstance(requireNotNull(app.applicationContext))
                    ?.favMovieDao()?.countMovieById(id)) > 0)
        }
        /*AppExecutors.Companion.getInstance().diskIO().execute(Runnable {
            val id = Objects.requireNonNull(_movie.value).getId()
            isFav.postValue(FavMovieDatabase.Companion.getInstance(getApplication<Application>())!!.favMovieDao()
                    .countMovieById(id) > 0)
        })*/
    }

    @Synchronized
    fun addToFavs() {
        if (_isFav.value != null && !_isFav.value!!) {
            viewModelScope.launch {
                FavMovieDatabase.getInstance(app.applicationContext)!!.favMovieDao()
                        .addMovieToFavorites(movie.value!!)
                _isFav.postValue(true)
            }
        }
    }

    @Synchronized
    fun removeFromFavs() {
        if (_isFav.value != null && _isFav.value!!) {
            viewModelScope.launch {
                FavMovieDatabase.getInstance(app.applicationContext)!!.favMovieDao()
                        .removeMovieFromFavorites(movie.value!!)
                _isFav.postValue(false)
            }
        }
    }

    init {
        initializeIsFav()
    }
}