package com.android.example.popularmovies.ui.detail.fragments.reviews;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.android.example.popularmovies.databinding.FragmentReviewsBinding;
import com.android.example.popularmovies.ui.detail.fragments.BaseDetailFragment;
import com.android.example.popularmovies.utils.json.MovieReview;

public class ReviewsFragment extends BaseDetailFragment implements ReviewsAdapter.ReviewOnClickListener {

    private ReviewsViewModel reviewsViewModel;
    private FragmentReviewsBinding binding;

    public static ReviewsFragment newInstance() {
        return new ReviewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reviewsViewModel = new ViewModelProvider(this).get(ReviewsViewModel.class);
        reviewsViewModel.getLoadingStatus().observe(getViewLifecycleOwner(), status -> {
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

    }

    @Override
    public void onListItemClick(MovieReview review) {
        Intent intent = new Intent(Intent.ACTION_VIEW, review.getUrlAsUri());
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
