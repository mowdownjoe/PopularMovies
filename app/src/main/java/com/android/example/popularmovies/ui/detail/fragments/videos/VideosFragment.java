package com.android.example.popularmovies.ui.detail.fragments.videos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.example.popularmovies.databinding.VideosFragmentBinding;

public class VideosFragment extends Fragment {

    private VideosViewModel mViewModel;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VideosViewModel.class);
        // TODO: Use the ViewModel
    }

}
