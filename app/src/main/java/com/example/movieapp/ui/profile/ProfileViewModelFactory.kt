package com.example.movieapp.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.data.profile.ProfileStore
import com.example.movieapp.domain.profile.ProfileRepository

class ProfileViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ProfileRepository::class.java).newInstance(
            ProfileRepository(
                ProfileStore.getProfileStore(context)
            )
        )
    }
}