package com.android.example.popularmovies.ui.detail.fragments.videos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.example.popularmovies.R
import com.android.example.popularmovies.databinding.FragmentVideosBinding
import com.android.example.popularmovies.ui.detail.fragments.BaseDetailFragment
import com.android.example.popularmovies.ui.detail.fragments.videos.VideosAdapter.VideoOnClickListener
import com.android.example.popularmovies.utils.json.MovieVideo
import com.android.example.popularmovies.utils.network.LoadingStatus

class VideosFragment : BaseDetailFragment(), VideoOnClickListener {
    private val viewModel by viewModels<VideosViewModel>()
    private lateinit var binding: FragmentVideosBinding
    private lateinit var adapter: VideosAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentVideosBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvVideoList.layoutManager = layoutManager
        adapter = VideosAdapter(this)
        binding.rvVideoList.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.status.observe(viewLifecycleOwner, Observer { status: LoadingStatus? ->
            when (status) {
                LoadingStatus.ERROR -> {
                    binding.tvErrorText.visibility = View.VISIBLE
                    binding.llVideoLayout.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                }
                LoadingStatus.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvErrorText.visibility = View.INVISIBLE
                    binding.llVideoLayout.visibility = View.INVISIBLE
                }
                LoadingStatus.DONE -> {
                    binding.llVideoLayout.visibility = View.VISIBLE
                    binding.tvErrorText.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        })
        viewModel!!.videos.observe(viewLifecycleOwner, Observer { movieVideos: List<MovieVideo>? ->
            if (movieVideos != null && movieVideos.isNotEmpty()) {
                adapter.setVideos(movieVideos)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val movie = movie
        if (movie != null) {
            viewModel.fetchVideos(getString(R.string.api_key_v3), movie.id)
        } else {
            viewModel.setStatus(LoadingStatus.ERROR)
            Log.e(javaClass.simpleName, "Null movie passed to fragment.")
        }
    }

    override fun onListItemClick(video: MovieVideo) {
        val intent = Intent(Intent.ACTION_VIEW, video.videoUri)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance(): VideosFragment {
            return VideosFragment()
        }
    }
}