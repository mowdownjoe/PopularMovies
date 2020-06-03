package com.android.example.popularmovies.ui;

public enum PosterSizes {
    THUMBNAIL(92),
    TINY(154),
    SMALL(185),
    NORMAL(342),
    LARGE(500),
    EXTRA_LARGE(780),
    ORIGINAL(100000);

    public final int size;

    PosterSizes(int size) {
        this.size = size;
    }
}
