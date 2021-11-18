package com.example.movieapp.domain.movie.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @SerializedName("poster_path") val posterPath : String? = null,
    val title: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("release_date") val releaseDate: String? = ""
) : Parcelable
