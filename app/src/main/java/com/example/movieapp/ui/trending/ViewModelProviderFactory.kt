package com.example.movieapp.ui.trending

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.data.movie.retrofit.MovieDbApiProvider
import com.example.movieapp.data.movie.retrofit.RetrofitMovieDataSource
import com.example.movieapp.data.movie.room.AppDatabase
import com.example.movieapp.data.movie.room.RoomMovieDataSource
import com.example.movieapp.domain.movie.MovieRepository
import com.example.movieapp.util.NetworkConnectivity

class ViewModelProviderFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(
                MovieRepository::class.java,
            )
            .newInstance(
                MovieRepository(
                    NetworkConnectivity(context),
                    RetrofitMovieDataSource(MovieDbApiProvider.getMovieDbApi()),
                    RoomMovieDataSource(AppDatabase.getDatabase(context).movieDao())
                )
            )
    }
}