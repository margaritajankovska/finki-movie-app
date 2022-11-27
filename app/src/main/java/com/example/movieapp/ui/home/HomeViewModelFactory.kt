package com.example.movieapp.ui.home

import android.content.Context
import android.net.Network
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.data.movie.retrofit.MovieDbApi
import com.example.movieapp.data.movie.retrofit.MovieDbApiProvider
import com.example.movieapp.data.movie.room.AppDatabase
import com.example.movieapp.data.movie.room.MovieDao
import com.example.movieapp.data.profile.ProfileStore
import com.example.movieapp.util.NetworkConnectivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            MovieDbApi::class.java,
            MovieDao::class.java,
            ProfileStore::class.java,
            NetworkConnectivity::class.java,
            CoroutineDispatcher::class.java
        )
            .newInstance(
                MovieDbApiProvider.getMovieDbApi(),
                AppDatabase.getDatabase(context).movieDao(),
                ProfileStore.getProfileStore(context),
                NetworkConnectivity(context),
                Dispatchers.IO

            )
    }
}