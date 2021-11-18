package com.example.movieapp.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.databinding.ActivitySearchBinding
import com.example.movieapp.ui.MoviesAdapter
import com.example.movieapp.ui.trending.ViewModelProviderFactory

class SearchActivity : AppCompatActivity() {

    private val moviesAdapter = MoviesAdapter()

    private lateinit var viewModel: SearchViewModel

    private lateinit var binding : ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = ViewModelProviderFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.suggestions.adapter = moviesAdapter

        binding.search.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.search(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        viewModel.getMoviesLiveData().observe(this) {
            moviesAdapter.updateMovies(it)
        }
    }
}