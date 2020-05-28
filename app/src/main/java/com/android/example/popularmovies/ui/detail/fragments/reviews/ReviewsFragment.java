package com.android.example.popularmovies.ui.detail.fragments.reviews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.example.popularmovies.LoadingStatus;
import com.android.example.popularmovies.R;
import com.android.example.popularmovies.database.FavMovieEntry;
import com.android.example.popularmovies.databinding.FragmentReviewsBinding;
import com.android.example.popularmovies.ui.detail.fragments.BaseDetailFragment;
import com.android.example.popularmovies.utils.json.MovieReview;

public class ReviewsFragment extends BaseDetailFragment implements ReviewsAdapter.ReviewOnClickListener {

    private ReviewsViewModel viewModel;
    private FragmentReviewsBinding binding;
    private ReviewsAdapter adapter;

    public static ReviewsFragment newInstance() {
        return new ReviewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewsBinding.inflate(inflater, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rvReviewList.setLayoutManager(layoutManager);

        adapter = new ReviewsAdapter(this);
        binding.rvReviewList.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ReviewsViewModel.class);
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            switch (status){
                case ERROR:
                    binding.tvErrorText.setVisibility(View.VISIBLE);
                    binding.llReviewLayout.setVisibility(View.INVISIBLE);
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    break;
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.tvErrorText.setVisibility(View.INVISIBLE);
                    binding.llReviewLayout.setVisibility(View.INVISIBLE);
                    break;
                case DONE:
                    binding.llReviewLayout.setVisibility(View.VISIBLE);
                    binding.tvErrorText.setVisibility(View.INVISIBLE);
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    break;
            }
        });
        viewModel.getReviews().observe(getViewLifecycleOwner(), movieReviews -> {
            if (movieReviews != null && !movieReviews.isEmpty()) {
                adapter.setReviews(movieReviews);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FavMovieEntry movie = getMovie();
        if (movie != null) {
            viewModel.fetchReviews(getString(R.string.api_key_v3), movie.getId());
        } else {
            viewModel.setStatus(LoadingStatus.ERROR);
            Log.e(getClass().getSimpleName(), "Null movie passed to fragment.");
        }
    }

    @Override
    public void onListItemClick(MovieReview review) {
        Intent intent = new Intent(Intent.ACTION_VIEW, review.getUrlAsUri())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
