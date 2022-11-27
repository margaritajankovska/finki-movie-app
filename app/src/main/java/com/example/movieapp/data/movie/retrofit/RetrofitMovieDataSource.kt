package com.example.movieapp.data.movie.retrofit

import com.example.movieapp.domain.movie.RemoteMovieDataSource
import com.example.movieapp.domain.movie.model.Genre
import com.example.movieapp.domain.movie.model.Movie
import com.example.movieapp.exception.GeneralException

class RetrofitMovieDataSource(private val movieDbApi: MovieDbApi) : RemoteMovieDataSource {

    override suspend fun search(query: String): List<Movie> {
        val movieResponse = movieDbApi.search(query)
        val responseBody = movieResponse.body()
        if (movieResponse.isSuccessful && responseBody != null) {
            return responseBody.results
        }
        throw GeneralException(movieResponse.message())
    }

    override suspend fun getTrending(): List<Movie> {
        val movieResponse = movieDbApi.getTrending()
        val responseBody = movieResponse.body()
        if (movieResponse.isSuccessful && responseBody != null) {
            return responseBody.results
        }
        throw GeneralException(movieResponse.message())
    }

    override suspend fun getByGenres(genres: String): List<Movie> {
        val movieResponse = movieDbApi.getByGenres(genres)
        val responseBody = movieResponse.body()
        if (movieResponse.isSuccessful && responseBody != null) {
            return responseBody.results
        }
        throw GeneralException(movieResponse.message())
    }

    override suspend fun getGenres(): List<Genre> {
        val genreResponse = movieDbApi.getGenres()
        val responseBody = genreResponse.body()
        if (genreResponse.isSuccessful && responseBody != null) {
            return responseBody.genres
        }
        throw GeneralException(genreResponse.message())
    }

}