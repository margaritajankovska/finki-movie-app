package com.example.movieapp.data.movie.room

import com.example.movieapp.domain.movie.LocalMovieDataSource
import com.example.movieapp.domain.movie.model.Movie

class RoomMovieDataSource(private val movieDao: MovieDao) : LocalMovieDataSource {
    override suspend fun insert(movie: Movie) {
        movieDao.insert(movie)
    }

    override suspend fun saveAll(movies: List<Movie>) {
        for (movie in movies) {
            movieDao.insert(movie)
        }
    }

    override suspend fun delete(id: Int) {
        movieDao.delete(id)
    }

    override suspend fun getAll(): List<Movie> {
        return movieDao.getAll()
    }

    override fun searchMovies(query: String): List<Movie> {
        return movieDao.searchMovies(query)
    }
}