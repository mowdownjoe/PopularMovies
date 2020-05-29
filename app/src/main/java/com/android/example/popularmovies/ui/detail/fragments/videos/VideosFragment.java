package com.android.example.popularmovies.ui.detail.fragments.videos;

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
import com.android.example.popularmovies.databinding.FragmentVideosBinding;
import com.android.example.popularmovies.ui.detail.fragments.BaseDetailFragment;
import com.android.example.popularmovies.utils.json.MovieVideo;

public class VideosFragment extends BaseDetailFragment implements VideosAdapter.VideoOnClickListener {

    private VideosViewModel viewModel;
    private FragmentVideosBinding binding;
    private VideosAdapter adapter;

    public static VideosFragment newInstance() {
        return new VideosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVideosBinding.inflate(inflater, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rvVideoList.setLayoutManager(layoutManager);

        adapter = new VideosAdapter(this);
        binding.rvVideoList.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(VideosViewModel.class);
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
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
        viewModel.getVideos().observe(getViewLifecycleOwner(), movieVideos -> {
            if (movieVideos != null && !movieVideos.isEmpty()){
                adapter.setVideos(movieVideos);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FavMovieEntry movie = getMovie(); //TODO: Move to onViewCreated?
        if (movie != null) {
            viewModel.fetchVideos(getString(R.string.api_key_v3), movie.getId());
        } else {
            viewModel.setStatus(LoadingStatus.ERROR);
            Log.e(getClass().getSimpleName(), "Null movie passed to fragment.");
        }
    }

    @Override
    public void onListItemClick(MovieVideo video) {
        Intent intent = new Intent(Intent.ACTION_VIEW, video.getVideoUri())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
