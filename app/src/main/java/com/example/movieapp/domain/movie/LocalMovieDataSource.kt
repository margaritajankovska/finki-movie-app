package com.example.movieapp.domain.movie

import com.example.movieapp.domain.movie.model.Movie

interface LocalMovieDataSource {
    suspend fun insert(movie: Movie)

    suspend fun saveAll(movies: List<Movie>)

    suspend fun delete(id: Int)

    suspend fun getAll(): List<Movie>

    fun searchMovies(query: String): List<Movie>
}