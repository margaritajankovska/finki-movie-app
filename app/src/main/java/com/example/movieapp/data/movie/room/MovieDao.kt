package com.example.movieapp.data.movie.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.domain.movie.model.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Query("DELETE FROM movie WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM movie")
    suspend fun getAll(): List<Movie>

    @Query(
        "SELECT * FROM movie " +
                "WHERE title LIKE '%' || :query || '%' "
    )
    fun searchMovies(query: String): List<Movie>
}