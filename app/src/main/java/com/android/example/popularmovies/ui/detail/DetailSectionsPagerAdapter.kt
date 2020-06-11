package com.android.example.popularmovies.ui.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.example.popularmovies.ui.detail.fragments.main.DetailFragment
import com.android.example.popularmovies.ui.detail.fragments.reviews.ReviewsFragment
import com.android.example.popularmovies.ui.detail.fragments.videos.VideosFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class DetailSectionsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        return when (position) {
            0 -> DetailFragment.newInstance()
            1 -> ReviewsFragment.newInstance()
            2 -> VideosFragment.newInstance()
            else -> throw IndexOutOfBoundsException("Somehow requested a fragment index that does not exist.")
        }
    }

    // Show 3 total pages.
    private val count: Int
        get() =// Show 3 total pages.
            3

    override fun createFragment(position: Int): Fragment {
        return getItem(position)
    }

    override fun getItemCount(): Int {
        return count
    }
}