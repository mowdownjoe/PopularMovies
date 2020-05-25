package com.android.example.popularmovies.ui.detail.fragments.reviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.example.popularmovies.databinding.ReviewsFragmentBinding;

public class ReviewsFragment extends Fragment {

    private ReviewsViewModel mViewModel;
    private ReviewsFragmentBinding binding;

    public static ReviewsFragment newInstance() {
        return new ReviewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ReviewsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReviewsViewModel.class);
        // TODO: Use the ViewModel
    }

}
