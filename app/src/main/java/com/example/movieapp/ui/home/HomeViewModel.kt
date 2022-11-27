package com.example.movieapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.movie.MovieRepository
import com.example.movieapp.domain.movie.model.Movie
import com.example.movieapp.domain.profile.ProfileRepository
import com.example.movieapp.domain.profile.model.Profile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val profileRepository: ProfileRepository,
    private val movieRepository: MovieRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val moviesLiveData = MutableLiveData<List<Movie>>()

    private val profileLiveData = MutableLiveData<Profile>()

    fun getMoviesLiveData(): LiveData<List<Movie>> = moviesLiveData

    fun getProfileLiveData(): LiveData<Profile> = profileLiveData

    init {
        initializeData()
    }

    private fun initializeData() {
        viewModelScope.launch(ioDispatcher) {
            val profile = profileRepository.getProfile()
            profileLiveData.postValue(profile)
            if (profile.genres.isNotEmpty()) {
                moviesLiveData.postValue(movieRepository.getMoviesByGenres(profile.genres))
            }
        }
    }
}