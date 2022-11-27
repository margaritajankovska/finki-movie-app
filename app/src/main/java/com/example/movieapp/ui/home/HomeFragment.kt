package com.example.movieapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.movieapp.R
import com.example.movieapp.data.movie.retrofit.MovieDbApi
import com.example.movieapp.data.movie.retrofit.MovieDbApiProvider
import com.example.movieapp.data.movie.room.AppDatabase
import com.example.movieapp.data.movie.room.MovieDao
import com.example.movieapp.data.profile.ProfileStore
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.ui.MoviesAdapter
import com.example.movieapp.util.NetworkConnectivity
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var profileStore: ProfileStore
    private lateinit var movieDbApi: MovieDbApi
    private lateinit var movieDao: MovieDao
    private lateinit var movieAdapter: MoviesAdapter
    private lateinit var networkConnectivity: NetworkConnectivity

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        networkConnectivity = NetworkConnectivity(requireContext())

        movieAdapter = MoviesAdapter()
        binding.movies.adapter = movieAdapter

        profileStore = ProfileStore.getProfileStore(requireContext())
        movieDbApi = MovieDbApiProvider.getMovieDbApi()
        movieDao = AppDatabase.getDatabase(requireContext()).movieDao();
        initProfile()
        initMovies()

        return binding.root
    }

    private fun initProfile() {
        val profile = profileStore.getProfile()
        val message = if (profile?.genres.isNullOrEmpty()) {
            getString(R.string.home_update_preferences)
        } else {
            getString(R.string.home_hello_message, profile?.fullName)
        }
        binding.textHello.text = message
    }

    private fun initMovies() {
        lifecycleScope.launch {
            if (networkConnectivity.isNetworkAvailable) {
                val profileGenres = profileStore.getProfile()?.genres
                if (!profileGenres.isNullOrEmpty()) {
                    val movieResponse =
                        movieDbApi.getByGenres(getGenreIdsFromGenreStrings(profileGenres!!))
                    if (movieResponse.isSuccessful) {
                        val results = movieResponse.body()!!.results
                        for (result in results) {
                            movieDao.insert(result)
                        }
                        movieAdapter.updateMovies(results)
                    }
                }
            } else {
                movieAdapter.updateMovies(movieDao.getAll())
            }
        }
    }

    private suspend fun getGenreIdsFromGenreStrings(genres: List<String>): String {
        val genreIds = ArrayList<Int>()
        val genresResponse = movieDbApi.getGenres()
        for (genre in genres) {
            for (movieDbGenre in genresResponse.body()!!.genres) {
                if (movieDbGenre.name.equals(genre, ignoreCase = true)) {
                    genreIds.add(movieDbGenre.id)
                    break
                }
            }
        }
        return genreIds.joinToString(",") {
            it.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}