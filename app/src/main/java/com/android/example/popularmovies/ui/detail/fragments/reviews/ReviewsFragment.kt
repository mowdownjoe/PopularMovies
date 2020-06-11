package com.android.example.popularmovies.ui.detail.fragments.reviews

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.example.popularmovies.R
import com.android.example.popularmovies.databinding.FragmentReviewsBinding
import com.android.example.popularmovies.ui.detail.fragments.BaseDetailFragment
import com.android.example.popularmovies.ui.detail.fragments.reviews.ReviewsAdapter.ReviewOnClickListener
import com.android.example.popularmovies.utils.json.MovieReview
import com.android.example.popularmovies.utils.network.LoadingStatus

class ReviewsFragment : BaseDetailFragment(), ReviewOnClickListener {
    private val viewModel by viewModels<ReviewsViewModel>()
    private lateinit var binding: FragmentReviewsBinding
    private lateinit var adapter: ReviewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentReviewsBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvReviewList.layoutManager = layoutManager
        adapter = ReviewsAdapter(requireContext(), this)
        binding.rvReviewList.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.status.observe(viewLifecycleOwner, Observer { status: LoadingStatus? ->
            when (status) {
                LoadingStatus.ERROR -> {
                    binding.tvErrorText.visibility = View.VISIBLE
                    binding.llReviewLayout.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                }
                LoadingStatus.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvErrorText.visibility = View.INVISIBLE
                    binding.llReviewLayout.visibility = View.INVISIBLE
                }
                LoadingStatus.DONE -> {
                    binding.llReviewLayout.visibility = View.VISIBLE
                    binding.tvErrorText.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        })
        viewModel.reviews.observe(viewLifecycleOwner, Observer { movieReviews: List<MovieReview>? ->
            if (movieReviews != null && movieReviews.isNotEmpty()) {
                adapter.setReviews(movieReviews)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val movie = movie
        if (movie != null) {
            viewModel.fetchReviews(getString(R.string.api_key_v3), movie.id)
        } else {
            viewModel.setStatus(LoadingStatus.ERROR)
            Log.e(javaClass.simpleName, "Null movie passed to fragment.")
        }
    }

    override fun onListItemClick(review: MovieReview?) {
        val intent = Intent(Intent.ACTION_VIEW, review?.urlAsUri)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance(): ReviewsFragment {
            return ReviewsFragment()
        }
    }
}