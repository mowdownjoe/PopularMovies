package com.android.example.popularmovies.ui.detail.fragments.videos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.example.popularmovies.R
import com.android.example.popularmovies.databinding.VideoListItemBinding
import com.android.example.popularmovies.ui.detail.fragments.videos.VideosAdapter.VideosViewHolder
import com.android.example.popularmovies.utils.json.MovieVideo

class VideosAdapter(private val onClickListener: VideoOnClickListener) : RecyclerView.Adapter<VideosViewHolder>() {
    private var mVideos: List<MovieVideo>? = null

    interface VideoOnClickListener {
        fun onListItemClick(video: MovieVideo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.video_list_item, parent, false)
        return VideosViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        if (mVideos != null) {
            holder.bind(mVideos!![position])
        }
    }

    override fun getItemCount(): Int = mVideos?.size ?: 0

    fun setVideos(videos: List<MovieVideo>?) {
        mVideos = videos
        notifyDataSetChanged()
    }

    inner class VideosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var binding: VideoListItemBinding = VideoListItemBinding.bind(itemView)
        fun bind(video: MovieVideo) {
            binding.tvVideoTitle.text = video.name
        }

        override fun onClick(v: View) {
            if (mVideos != null) {
                onClickListener.onListItemClick(mVideos!![adapterPosition])
            }
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

}