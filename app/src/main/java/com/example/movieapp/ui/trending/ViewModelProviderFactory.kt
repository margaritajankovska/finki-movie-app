package com.example.movieapp.ui.trending

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.data.movie.retrofit.MovieDbApiProvider
import com.example.movieapp.data.movie.room.AppDatabase
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
                    MovieDbApiProvider.getMovieDbApi(),
                    AppDatabase.getDatabase(context).movieDao()
                )
            )
    }
}