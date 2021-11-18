package com.example.movieapp.ui.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.databinding.FragmentTrendingBinding
import com.example.movieapp.ui.MoviesAdapter

class TrendingFragment : Fragment() {

    private lateinit var dashboardViewModel: TrendingViewModel

    private var _binding: FragmentTrendingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val moviesAdapter = MoviesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModelFactory = ViewModelProviderFactory(requireContext())
        dashboardViewModel =
            ViewModelProvider(this, viewModelFactory).get(TrendingViewModel::class.java)

        _binding = FragmentTrendingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.trendingMovies.adapter = moviesAdapter

        dashboardViewModel.getMoviesLiveData().observe(viewLifecycleOwner) {
            moviesAdapter.updateMovies(it)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}