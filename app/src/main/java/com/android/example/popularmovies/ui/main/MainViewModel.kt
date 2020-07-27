package com.android.example.popularmovies.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.android.example.popularmovies.database.FavMovieDatabase
import com.android.example.popularmovies.database.MovieEntry
import com.android.example.popularmovies.utils.network.LoadingStatus
import com.android.example.popularmovies.utils.network.MovieDbAPI
import com.android.example.popularmovies.utils.network.MovieResults
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    enum class SortOrder(val node: String) {
        POPULAR("popular"),
        TOP_RATED("top_rated"),
        NOW_PLAYING("now_playing"),
        FAVORITE("fav");
    }

    private var cachedResults: MovieResults? = null

    private val _sortOrder: MutableLiveData<SortOrder> = MutableLiveData()
    val sortOrder: LiveData<SortOrder>
        get() = _sortOrder
    private val _status: MutableLiveData<LoadingStatus> = MutableLiveData()
    val status: LiveData<LoadingStatus>
        get() = _status
    private val moviesFromNetwork: MutableLiveData<List<MovieEntry>> = MutableLiveData()
    private val moviesFromDb: LiveData<List<MovieEntry>> = FavMovieDatabase.getInstance(app)
            .favMovieDao().getAllFavorites()
    private val _movieList: MediatorLiveData<List<MovieEntry>> = MediatorLiveData()
    val movieList: LiveData<List<MovieEntry>>
        get() = _movieList

    init {
        _status.value = LoadingStatus.INIT
        _movieList.addSource(moviesFromNetwork) { movieEntries -> _movieList.setValue(movieEntries) }
    }

    fun setSortOrder(newOrder: SortOrder?) {
        _sortOrder.postValue(newOrder)
    }

    fun setStatus(newStatus: LoadingStatus?) {
        _status.postValue(newStatus)
    }

    fun fetchMovieData(apiKey: String, sortOrder: SortOrder) {
        require(sortOrder != SortOrder.FAVORITE)
        _movieList.removeSource(moviesFromDb)
        viewModelScope.launch {
            _status.postValue(LoadingStatus.LOADING)
            try {
                cachedResults = MovieDbAPI.apiService.getMovies(sortOrder.node, apiKey)
                moviesFromNetwork.postValue(cachedResults?.results)
                _status.postValue(LoadingStatus.DONE)
            } catch (e: Exception) {
                Log.e("FetchMovieData", "Error caught while fetching and parsing JSON", e)
                _status.postValue(LoadingStatus.ERROR)
            }
        }
    }

    fun fetchNextPage(apiKey: String){
        require(_sortOrder.value != SortOrder.FAVORITE)
        requireNotNull(cachedResults)
        require(cachedResults!!.page > cachedResults!!.totalPages)
        viewModelScope.launch {
            try {
                cachedResults = MovieDbAPI.apiService.getMovies(_sortOrder.value?.node!!, apiKey, cachedResults?.page!! +1)
                moviesFromNetwork.postValue(moviesFromNetwork.value!!.plus(cachedResults!!.results))
            } catch (e: Exception){
                Log.e("MainViewModel", "Failed to grab next page of data.", e)
            }
        }
    }

    fun loadFavoriteMovies() {
        _movieList.addSource(moviesFromDb) { movieEntries -> _movieList.setValue(movieEntries) }
        _movieList.value = moviesFromDb.value
    }
}