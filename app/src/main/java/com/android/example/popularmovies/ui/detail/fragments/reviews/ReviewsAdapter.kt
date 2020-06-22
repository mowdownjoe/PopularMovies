package com.android.example.popularmovies.ui.detail.fragments.reviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.example.popularmovies.R
import com.android.example.popularmovies.databinding.ReviewListItemBinding
import com.android.example.popularmovies.ui.detail.fragments.reviews.ReviewsAdapter.ReviewViewHolder
import com.android.example.popularmovies.utils.json.MovieReview
import io.noties.markwon.Markwon

class ReviewsAdapter(c: Context, private val listener: ReviewOnClickListener) : RecyclerView.Adapter<ReviewViewHolder>() {
    private var mReviews: List<MovieReview>? = null
    private var markwon = Markwon.create(c)

    interface ReviewOnClickListener {
        fun onListItemClick(review: MovieReview?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.review_list_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        if (mReviews != null) {
            holder.bind(mReviews!![position])
        }
    }

    override fun getItemCount(): Int = mReviews?.size ?: 0

    fun setReviews(reviews: List<MovieReview>?) {
        mReviews = reviews
        notifyDataSetChanged()
    }



    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var binding: ReviewListItemBinding = ReviewListItemBinding.bind(itemView)
        fun bind(review: MovieReview) {
            binding.tvAuthor.text = review.author
            markwon.setMarkdown(binding.tvReview, review.content)
        }

        override fun onClick(v: View) {
            if (mReviews != null) {
                listener.onListItemClick(mReviews!![adapterPosition])
            }
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}