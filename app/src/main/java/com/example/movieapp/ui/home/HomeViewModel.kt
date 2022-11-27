package com.example.movieapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.movie.retrofit.MovieDbApi
import com.example.movieapp.data.movie.room.MovieDao
import com.example.movieapp.data.profile.ProfileStore
import com.example.movieapp.domain.movie.model.Movie
import com.example.movieapp.domain.profile.model.Profile
import com.example.movieapp.util.NetworkConnectivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieDbApi: MovieDbApi,
    private val movieDao: MovieDao,
    private val profileStore: ProfileStore,
    private val networkConnectivity: NetworkConnectivity,
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
            val profile = profileStore.getProfile()?.apply { profileLiveData.postValue(this) }

            if (networkConnectivity.isNetworkAvailable) {
                if (!profile?.genres.isNullOrEmpty()) {
                    moviesLiveData.postValue(getMoviesByGenres(profile!!.genres))
                }
            } else {
                moviesLiveData.postValue(movieDao.getAll())
            }
        }
    }

    private suspend fun getMoviesByGenres(genres: List<String>): List<Movie> {
        val movieResponse = movieDbApi.getByGenres(getGenreIdsFromGenreStrings(genres))
        if (movieResponse.isSuccessful) {
            val results = movieResponse.body()!!.results
            for (result in results) {
                movieDao.insert(result)
            }
            return results
        }

        return movieDao.getAll()
    }

    private suspend fun getGenreIdsFromGenreStrings(genres: List<String>): String {
        val genreIds = ArrayList<Int>()
        val genresResponse = movieDbApi.getGenres()
        for (genre in genres) {
            for (movieDbGenre in genresResponse.body()!!.genres) {
                if (movieDbGenre.name.equals(genre, ignoreCase = true)) {
                    genreIds.add(movieDbGenre.id)
                    break
                }
            }
        }
        return genreIds.joinToString(",") {
            it.toString()
        }
    }
}