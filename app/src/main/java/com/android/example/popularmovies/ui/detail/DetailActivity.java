package com.android.example.popularmovies.ui.detail;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.example.popularmovies.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
