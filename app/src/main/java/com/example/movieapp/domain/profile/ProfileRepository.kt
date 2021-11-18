package com.example.movieapp.domain.profile

import com.example.movieapp.data.profile.ProfileStore
import com.example.movieapp.domain.profile.model.Profile

class ProfileRepository(private val profileStore: ProfileStore) {

    suspend fun getProfile(): Profile {
        val profile = profileStore.getProfile()
        return profile ?: Profile()
    }

    suspend fun saveProfile(profile: Profile): Boolean {
        return profileStore.saveProfile(profile)
    }
}