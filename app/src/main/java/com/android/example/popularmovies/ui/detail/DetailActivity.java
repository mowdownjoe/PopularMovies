package com.android.example.popularmovies.ui.detail;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.example.popularmovies.R;
import com.android.example.popularmovies.databinding.ActivityDetailBinding;
import com.android.example.popularmovies.ui.detail.temp.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayoutMediator;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);

        binding.viewPager.setAdapter(sectionsPagerAdapter);

        new TabLayoutMediator(binding.tabs, binding.viewPager, ((tab, position) -> {
                    //TODO
                })).attach();

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }
}