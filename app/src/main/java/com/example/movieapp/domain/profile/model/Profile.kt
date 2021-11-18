package com.example.movieapp.domain.profile.model

data class Profile(
    val fullName : String? = null,
    val imageName : String? = null,
    val genres : List<String> = listOf()
)
