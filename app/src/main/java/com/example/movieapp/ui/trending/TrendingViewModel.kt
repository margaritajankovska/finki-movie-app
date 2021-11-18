package com.example.movieapp.ui.trending

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.movie.MovieRepository
import com.example.movieapp.domain.movie.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrendingViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val moviesLiveData = MutableLiveData<List<Movie>>()

    fun getMoviesLiveData() : LiveData<List<Movie>> = moviesLiveData

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val movies = movieRepository.getTrendingMovies()
            moviesLiveData.postValue(movies)
        }
    }
}