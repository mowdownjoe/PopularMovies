package com.android.example.popularmovies.ui.detail.fragments.videos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.popularmovies.utils.json.MovieVideo
import com.android.example.popularmovies.utils.network.LoadingStatus
import com.android.example.popularmovies.utils.network.MovieDbAPI
import kotlinx.coroutines.launch

class VideosViewModel : ViewModel() {
    private val _status: MutableLiveData<LoadingStatus> = MutableLiveData()
    val status: LiveData<LoadingStatus>
        get() = _status
    private val _videos: MutableLiveData<List<MovieVideo>> = MutableLiveData()
    val videos: LiveData<List<MovieVideo>>
        get() = _videos

    init {
        _status.postValue(LoadingStatus.INIT)
    }

    fun fetchVideos(apiKey: String, movieId: Int) {
        _status.postValue(LoadingStatus.LOADING)
        viewModelScope.launch {
            try {
                _videos.postValue(MovieDbAPI.apiService.getMovieVideos(movieId, apiKey))
                _status.postValue(LoadingStatus.DONE)
            } catch (e: Exception){
                _status.postValue(LoadingStatus.ERROR)
            }
        }
    }

    fun setStatus(newStatus: LoadingStatus?) = _status.postValue(newStatus)
}