package com.example.movieapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.profile.ProfileRepository
import com.example.movieapp.domain.profile.model.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    private val profileLiveData = MutableLiveData<Profile>()

    private val saveStateLiveData = MutableLiveData<Boolean>()

    fun getProfileLiveData(): LiveData<Profile> {
        return profileLiveData
    }

    fun getSaveStateLiveData(): LiveData<Boolean> {
        return saveStateLiveData
    }

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            profileLiveData.postValue(profileRepository.getProfile())
        }
    }

    fun saveProfile(fullName: String, imageName: String, genres: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            saveStateLiveData.postValue(
                profileRepository.saveProfile(
                    Profile(
                        fullName,
                        imageName,
                        genres
                    )
                )
            )
        }
    }

}