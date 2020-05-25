package com.android.example.popularmovies.ui.detail.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.android.example.popularmovies.R;
import com.android.example.popularmovies.database.FavMovieEntry;
import com.android.example.popularmovies.databinding.FragmentDetailBinding;
import com.android.example.popularmovies.ui.detail.DetailViewModel;
import com.android.example.popularmovies.ui.detail.DetailViewModelFactory;
import com.android.example.popularmovies.ui.detail.fragments.BaseDetailFragment;
import com.squareup.picasso.Picasso;

public class DetailFragment extends BaseDetailFragment {

    private FragmentDetailBinding binding;
    private DetailViewModel viewModel;

    public static DetailFragment newInstance() {

        Bundle args = new Bundle();

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate ViewBinding and define UI
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Create Viewmodel
        FavMovieEntry movie = getMovie();
        if (movie != null){
            viewModel = new ViewModelProvider(requireActivity(), DetailViewModelFactory
                    .getInstance(requireActivity().getApplication(), movie))
                    .get(DetailViewModel.class);
        } else {
            requireActivity().finish();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Propagate data from ViewModel into UI
        binding.tvMovieTitle.setText(viewModel.getMovie().getValue().getTitle());
        //Assuming that if one extra is included from explicit intent, others will be.
        binding.tvDesc.setText(viewModel.getMovie().getValue().getDescription());
        binding.tvReleaseDate.setText(getString(R.string.date_detail, viewModel.getMovie().getValue().getReleaseDate()));
        binding.tvRating.setText(getString(R.string.rating_detail, viewModel.getMovie().getValue().getUserRating()));
        Picasso.get().load(viewModel.getMovie().getValue().getPosterUrl())
                .placeholder(R.drawable.loading_poster)
                .error(R.drawable.error_poster)
                .into(binding.ivPosterThumbnail);
    }
}
