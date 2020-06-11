package com.android.example.popularmovies.ui.main

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.example.popularmovies.R
import com.android.example.popularmovies.database.MovieEntry
import com.android.example.popularmovies.ui.PosterSizes
import com.android.example.popularmovies.ui.main.PosterAdapter.PosterViewHolder
import com.squareup.picasso.Picasso

class PosterAdapter(private val onClickListener: PosterOnClickListener) : RecyclerView.Adapter<PosterViewHolder>() {
    //TODO Implement paging using androidx.paging library
    private var movieList: List<MovieEntry>? = null

    interface PosterOnClickListener {
        fun onListItemClick(movie: MovieEntry)
    }

    fun setMovieData(movies: List<MovieEntry>?) {
        movieList = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.poster_grid_item, parent, false)
        return PosterViewHolder(view)
    }

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        if (movieList != null) {
            holder.bind(movieList!![position]!!.getPosterUri(PosterSizes.LARGE))
        }
    }

    override fun getItemCount(): Int {
        return if (movieList != null) {
            movieList!!.size
        } else {
            0
        }
    }

    inner class PosterViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val _onClick = "PosterViewHolderOnClick"
        private var posterImage: ImageView = itemView.findViewById(R.id.iv_poster_image)
        fun bind(url: String?) {
            val uri = Uri.parse(url)
            bind(uri)
        }

        fun bind(uri: Uri?) {
            Log.v("PosterViewHolder.bind", "Loading from URI: " + uri.toString())
            Picasso.get()
                    .load(uri)
                    .placeholder(R.drawable.loading_poster)
                    .error(R.drawable.error_poster)
                    .into(posterImage)
        }

        override fun onClick(v: View) {
            Log.v(_onClick, "Received click on item $adapterPosition")
            if (movieList != null) {
                onClickListener.onListItemClick(movieList!![adapterPosition])
            } else {
                Log.w(_onClick, "movieData is null")
            }
        }



        init {
            itemView.setOnClickListener(this)
        }
    }

}