package com.example.movieapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.domain.movie.model.Movie
import com.example.movieapp.ui.MoviesAdapter.MovieViewHolder
import java.util.*

class MoviesAdapter(private val movies: ArrayList<Movie> = ArrayList<Movie>()) :
    RecyclerView.Adapter<MovieViewHolder>() {

    fun updateMovies(movies: List<Movie>?) {
        this.movies.clear()
        if (movies != null) {
            this.movies.addAll(movies)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var imageView: ImageView = view.findViewById(R.id.movie_image)
        private var title: TextView = view.findViewById(R.id.movie_title)

        fun bind(movie: Movie) {
            Glide.with(itemView)
                .load(
                    "https://image.tmdb.org/t/p/w185" +
                            movie.posterPath
                )
                .centerCrop()
                .placeholder(R.drawable.ic_movie)
                .into(imageView)
            title.text = movie.title
        }
    }
}