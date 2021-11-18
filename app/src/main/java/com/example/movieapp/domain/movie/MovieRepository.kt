package com.example.movieapp.domain.movie

import com.example.movieapp.data.movie.retrofit.MovieDbApi
import com.example.movieapp.data.movie.room.MovieDao
import com.example.movieapp.domain.movie.model.Movie
import com.example.movieapp.util.NetworkConnectivity

class MovieRepository(
    private val networkConnectivity: NetworkConnectivity,
    private val movieDbApi: MovieDbApi,
    private val movieDao: MovieDao
) {

    suspend fun getTrendingMovies(): List<Movie> {
        if (networkConnectivity.isNetworkAvailable) {
            val movieResponse = movieDbApi.getTrending()
            if (movieResponse.isSuccessful) {
                val results = movieResponse.body()!!.results
                for (result in results) {
                    movieDao.insert(result)
                }
                return results
            }
        }
        return movieDao.getAll()
    }

    suspend fun searchMovies(query: String): List<Movie> {
        if (networkConnectivity.isNetworkAvailable) {
            val movieResponse = movieDbApi.search(query)
            if (movieResponse.isSuccessful) {
                val results = movieResponse.body()!!.results
                for (result in results) {
                    movieDao.insert(result)
                }
                return results
            }
        }
        return movieDao.searchMovies(query)
    }

    suspend fun getMoviesByGenres(genres: List<String>): List<Movie> {
        if (networkConnectivity.isNetworkAvailable) {
            val movieResponse = movieDbApi.getByGenres(getGenreIdsFromGenreStrings(genres))
            if (movieResponse.isSuccessful) {
                val results = movieResponse.body()!!.results
                for (result in results) {
                    movieDao.insert(result)
                }
                return results
            }
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