package com.example.movieapp.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.data.movie.retrofit.MovieDbApiProvider
import com.example.movieapp.data.movie.room.AppDatabase
import com.example.movieapp.data.profile.ProfileStore
import com.example.movieapp.domain.movie.MovieRepository
import com.example.movieapp.domain.profile.ProfileRepository
import com.example.movieapp.util.NetworkConnectivity

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ProfileRepository::class.java, MovieRepository::class.java)
            .newInstance(
                ProfileRepository(ProfileStore.getProfileStore(context)), MovieRepository(
                    NetworkConnectivity(context),
                    MovieDbApiProvider.getMovieDbApi(),
                    AppDatabase.getDatabase(context).movieDao()
                )
            )
    }
}