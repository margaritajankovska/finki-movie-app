package com.example.movieapp.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.data.movie.retrofit.MovieDbApiProvider
import com.example.movieapp.data.movie.retrofit.RetrofitMovieDataSource
import com.example.movieapp.data.movie.room.AppDatabase
import com.example.movieapp.data.movie.room.RoomMovieDataSource
import com.example.movieapp.data.profile.ProfileStore
import com.example.movieapp.domain.movie.MovieRepository
import com.example.movieapp.domain.profile.ProfileRepository
import com.example.movieapp.util.NetworkConnectivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            ProfileRepository::class.java,
            MovieRepository::class.java,
            CoroutineDispatcher::class.java
        )
            .newInstance(
                ProfileRepository(ProfileStore.getProfileStore(context)),
                MovieRepository(
                    NetworkConnectivity(context),
                    RetrofitMovieDataSource(MovieDbApiProvider.getMovieDbApi()),
                    RoomMovieDataSource(AppDatabase.getDatabase(context).movieDao())
                ),
                Dispatchers.IO
            )
    }
}