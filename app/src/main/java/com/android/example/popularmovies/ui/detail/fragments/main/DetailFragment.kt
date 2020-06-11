package com.android.example.popularmovies.ui.detail.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.android.example.popularmovies.R
import com.android.example.popularmovies.databinding.FragmentDetailBinding
import com.android.example.popularmovies.ui.PosterSizes
import com.android.example.popularmovies.ui.detail.DetailViewModel
import com.android.example.popularmovies.ui.detail.DetailViewModelFactory
import com.android.example.popularmovies.ui.detail.fragments.BaseDetailFragment
import com.squareup.picasso.Picasso

class DetailFragment : BaseDetailFragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Inflate ViewBinding and define UI
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Create Viewmodel
        val movie = movie
        if (movie != null) {
            viewModel = ViewModelProvider(requireActivity(), DetailViewModelFactory.getInstance(requireActivity().application, movie)!!)
                    .get(DetailViewModel::class.java)
        } else {
            requireActivity().finish()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Propagate data from ViewModel into UI
        binding.tvMovieTitle.text = viewModel.movie.value?.title
        binding.tvDesc.text = viewModel.movie.value?.description
        binding.tvReleaseDate.text = getString(R.string.date_detail, viewModel.movie.value?.releaseDate)
        binding.tvRating.text = getString(R.string.rating_detail, viewModel.movie.value?.userRating)
        Picasso.get().load(viewModel.movie.value!!.getPosterUri(PosterSizes.SMALL))
                .placeholder(R.drawable.loading_poster)
                .error(R.drawable.error_poster)
                .into(binding.ivPosterThumbnail)
    }

    companion object {
        fun newInstance(): DetailFragment {
            val args = Bundle()
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}