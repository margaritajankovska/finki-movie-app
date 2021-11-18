package com.example.movieapp.domain.movie.model

data class MovieResponse(
    val page: Int,
    val results: List<Movie>
)