package com.example.movieapp.data.movie.retrofit

import com.example.movieapp.domain.movie.model.Genre
import com.example.movieapp.domain.movie.model.GenresResponse
import com.example.movieapp.domain.movie.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDbApi {
    @GET("trending/movie/week")
    suspend fun getTrending(): Response<MovieResponse>

    @GET("search/movie")
    suspend fun search(@Query("query")  query : String): Response<MovieResponse>

    @GET("discover/movie")
    suspend fun getByGenres(@Query("with_genres")  genres : String): Response<MovieResponse>

    @GET("genre/movie/list")
    suspend fun getGenres(): Response<GenresResponse>
}