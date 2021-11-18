package com.example.movieapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.domain.profile.model.Profile
import com.example.movieapp.ui.MoviesAdapter

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModelFactory = HomeViewModelFactory(requireContext())
        homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val adapter = MoviesAdapter()
        binding.movies.adapter = adapter

        homeViewModel.getProfileLiveData().observe(viewLifecycleOwner) {
            initMessage(it)
        }

        homeViewModel.getMoviesLiveData().observe(viewLifecycleOwner) {
            adapter.updateMovies(it)
        }

        return binding.root
    }

    private fun initMessage(profile: Profile) {
        val message = if (profile.genres.isEmpty()) {
            getString(R.string.home_update_preferences)
        } else {
            getString(R.string.home_hello_message, profile.fullName)
        }
        binding.textHello.text = message
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}