package com.android.example.popularmovies.ui.detail.fragments.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.popularmovies.utils.json.MovieReview
import com.android.example.popularmovies.utils.network.LoadingStatus
import com.android.example.popularmovies.utils.network.MovieDbAPI
import kotlinx.coroutines.launch

class ReviewsViewModel : ViewModel() {
    private val _status: MutableLiveData<LoadingStatus> = MutableLiveData()
    val status: LiveData<LoadingStatus> get() = _status
    private val _reviews: MutableLiveData<List<MovieReview>> = MutableLiveData()
    val reviews: LiveData<List<MovieReview>> get() = _reviews

    fun setStatus(newStatus: LoadingStatus) {
        _status.postValue(newStatus)
    }

    fun fetchReviews(apiKey: String, movieId: Int) {
        _status.postValue(LoadingStatus.LOADING)
        viewModelScope.launch {
            try {
                _reviews.postValue(MovieDbAPI.apiService.getMovieReviews(movieId, apiKey))
                _status.postValue(LoadingStatus.DONE)
            } catch (e: Exception) {
                _status.postValue(LoadingStatus.ERROR)
            }
        }
    }

    init {
        _status.postValue(LoadingStatus.INIT)
    }
}