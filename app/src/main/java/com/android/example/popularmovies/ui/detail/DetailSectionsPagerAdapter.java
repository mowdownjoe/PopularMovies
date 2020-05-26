package com.android.example.popularmovies.ui.detail;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.example.popularmovies.R;
import com.android.example.popularmovies.ui.detail.fragments.main.DetailFragment;
import com.android.example.popularmovies.ui.detail.fragments.reviews.ReviewsFragment;
import com.android.example.popularmovies.ui.detail.fragments.videos.VideosFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class DetailSectionsPagerAdapter extends FragmentStateAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public DetailSectionsPagerAdapter(@NonNull FragmentActivity activity) {
        super(activity);
        mContext = activity;
    }

    private Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        switch (position){
            case 0:
                return DetailFragment.newInstance();
            case 1:
                return ReviewsFragment.newInstance();
            case 2:
                return VideosFragment.newInstance();
            default:
                throw new IndexOutOfBoundsException("Somehow requested a fragment index that does not exist.");
        }
    }

    @Nullable
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    private int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return getItem(position);
    }

    @Override
    public int getItemCount() {
        return getCount();
    }
}