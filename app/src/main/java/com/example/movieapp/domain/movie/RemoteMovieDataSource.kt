package com.example.movieapp.domain.movie

import com.example.movieapp.domain.movie.model.Genre
import com.example.movieapp.domain.movie.model.Movie

interface RemoteMovieDataSource {
    suspend fun search(query : String): List<Movie>

    suspend fun getTrending(): List<Movie>

    suspend fun getByGenres(genres : String): List<Movie>

    suspend fun getGenres(): List<Genre>
}