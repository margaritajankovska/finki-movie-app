package com.example.movieapp.data.profile

import android.content.Context
import android.content.SharedPreferences
import com.example.movieapp.domain.profile.model.Profile
import com.google.gson.Gson

class ProfileStore(private val sharedPreferences: SharedPreferences) {
    private val gson = Gson()

    fun saveProfile(profile: Profile) : Boolean {
        return sharedPreferences.edit().putString(PROFILE_KEY, gson.toJson(profile)).commit()
    }

    fun getProfile(): Profile? {
        val profileString = sharedPreferences.getString(PROFILE_KEY, "")
        return gson.fromJson(profileString, Profile::class.java)
    }

    companion object {
        private const val PROFILE_STORE_PREFERENCES_KEy = "profile_store_preference"
        private const val PROFILE_KEY = "profile_key"

        @Volatile
        private var INSTANCE: ProfileStore? = null

        @JvmStatic
        fun getProfileStore(context: Context): ProfileStore {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = ProfileStore(
                    context.getSharedPreferences(
                        PROFILE_STORE_PREFERENCES_KEy,
                        Context.MODE_PRIVATE
                    )
                )
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}