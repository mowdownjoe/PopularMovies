package com.android.example.popularmovies.ui.detail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.example.popularmovies.R
import com.android.example.popularmovies.database.MovieEntry
import com.android.example.popularmovies.databinding.ActivityDetailBinding
import com.android.example.popularmovies.utils.json.JsonUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var mediator: TabLayoutMediator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (binding.tabs != null) {
            val sectionsPagerAdapter = DetailSectionsPagerAdapter(this)
            binding.viewPager?.adapter = sectionsPagerAdapter //Must exist in layout if tabs exist.
            mediator = TabLayoutMediator(binding.tabs!!, binding.viewPager!!,
                    TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                        when (position) {
                            0 -> tab.setText(R.string.tab_header_details)
                            1 -> tab.setText(R.string.tab_header_reviews)
                            2 -> tab.setText(R.string.tab_header_videos)
                        }
                    })
            mediator?.attach()
        }
        val movie = movie
        viewModel = if (movie != null) {
            ViewModelProvider(this,
                    DetailViewModelFactory.getInstance(application, movie)!!)
                    .get(DetailViewModel::class.java)
        } else {
            finish()
            return
        }
        viewModel.isFav.observe(this, Observer { isFav: Boolean? ->
            if (requireNotNull(isFav)) {
                binding.fab.setImageResource(R.drawable.ic_unfav_action)
            } else {
                binding.fab.setImageResource(R.drawable.ic_fav_action)
            }
        })
        binding.fab.setOnClickListener {
            val isFav = requireNotNull(viewModel.isFav.value) { //If null:
                Log.e(javaClass.simpleName, "ViewModel has not initialized isFav.")
                return@setOnClickListener
            }
            if (isFav) {
                viewModel.removeFromFavs()
            } else {
                viewModel.addToFavs()
            }
        }
        binding.fab.setOnLongClickListener {
            val isFav = requireNotNull(viewModel.isFav.value) {
                Log.e(javaClass.simpleName, "ViewModel has not initialized isFav.")
                return@setOnLongClickListener false
            }
            if (isFav) {
                Toast.makeText(this, R.string.remove_fav_hint, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.add_fav_hint, Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediator != null) {
            mediator?.detach()
            mediator = null
        }
    }

    val movie: MovieEntry?
        get() {
            return if (intent.hasExtra(JsonUtils.TITLE)) {
                MovieEntry(
                        intent.getIntExtra(JsonUtils.MOVIE_ID, -1),
                        intent.getStringExtra(JsonUtils.TITLE),
                        intent.getStringExtra(JsonUtils.OVERVIEW),
                        intent.getDoubleExtra(JsonUtils.VOTE_AVERAGE, 0.0),
                        intent.getStringExtra(JsonUtils.POSTER_PATH),
                        intent.getSerializableExtra(JsonUtils.RELEASE_DATE) as Date
                )
            } else {
                null
            }
        }
}