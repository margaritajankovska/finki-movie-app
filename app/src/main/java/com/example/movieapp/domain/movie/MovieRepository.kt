package com.example.movieapp.domain.movie

import com.example.movieapp.domain.movie.model.Movie
import com.example.movieapp.util.NetworkConnectivity

class MovieRepository(
    private val networkConnectivity: NetworkConnectivity,
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val localMovieDataSource: LocalMovieDataSource
) {

    suspend fun getTrendingMovies(): List<Movie> {
        if (networkConnectivity.isNetworkAvailable) {
            return remoteMovieDataSource.getTrending()
                .apply { localMovieDataSource.saveAll(this) }
        }
        return localMovieDataSource.getAll()
    }

    suspend fun searchMovies(query: String): List<Movie> {
        if (networkConnectivity.isNetworkAvailable) {
            return remoteMovieDataSource.search(query)
                .apply { localMovieDataSource.saveAll(this) }
        }
        return localMovieDataSource.searchMovies(query)
    }

    suspend fun getMoviesByGenres(genres: List<String>): List<Movie> {
        if (networkConnectivity.isNetworkAvailable) {
            return remoteMovieDataSource.getByGenres(getGenreIdsFromGenreStrings(genres))
                .apply { localMovieDataSource.saveAll(this) }
        }
        return localMovieDataSource.getAll()
    }

    private suspend fun getGenreIdsFromGenreStrings(genres: List<String>): String {
        val genreIds = ArrayList<Int>()
        val genresResponse = remoteMovieDataSource.getGenres()
        for (genre in genres) {
            for (movieDbGenre in genresResponse) {
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