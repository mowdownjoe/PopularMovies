package com.android.example.popularmovies.ui.detail.fragments.videos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.android.example.popularmovies.databinding.VideosFragmentBinding;
import com.android.example.popularmovies.ui.detail.fragments.BaseDetailFragment;

public class VideosFragment extends BaseDetailFragment {

    private VideosViewModel videosViewModel;
    private VideosFragmentBinding binding;

    public static VideosFragment newInstance() {
        return new VideosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = VideosFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        videosViewModel = new ViewModelProvider(this).get(VideosViewModel.class);
        videosViewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            switch (status){
                case ERROR:
                    binding.tvErrorText.setVisibility(View.VISIBLE);
                    binding.llVideoLayout.setVisibility(View.INVISIBLE);
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    break;
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.tvErrorText.setVisibility(View.INVISIBLE);
                    binding.llVideoLayout.setVisibility(View.INVISIBLE);
                    break;
                case DONE:
                    binding.llVideoLayout.setVisibility(View.VISIBLE);
                    binding.tvErrorText.setVisibility(View.INVISIBLE);
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    break;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
