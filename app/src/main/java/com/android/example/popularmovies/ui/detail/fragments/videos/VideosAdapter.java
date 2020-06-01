package com.android.example.popularmovies.ui.detail.fragments.videos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.popularmovies.R;
import com.android.example.popularmovies.databinding.VideoListItemBinding;
import com.android.example.popularmovies.utils.json.MovieVideo;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {

    @Nullable
    private List<MovieVideo> mVideos;

    public interface VideoOnClickListener{
        void onListItemClick(MovieVideo video);
    }

    private VideoOnClickListener onClickListener;

    public VideosAdapter(VideoOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public VideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video_list_item, parent, false);
        return new VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosViewHolder holder, int position) {
        if (mVideos != null) {
            holder.bind(mVideos.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (mVideos != null) {
            return mVideos.size();
        } else {
            return 0;
        }
    }

    public void setVideos(List<MovieVideo> videos){
        mVideos = videos;
        notifyDataSetChanged();
    }

    class VideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        VideoListItemBinding binding;

        public VideosViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = VideoListItemBinding.bind(itemView);
            itemView.setOnClickListener(this);
        }

        void bind(MovieVideo video){
            binding.tvVideoTitle.setText(video.getName());
        }

        @Override
        public void onClick(View v) {
            if (mVideos != null){
                onClickListener.onListItemClick(mVideos.get(getAdapterPosition()));
            }
        }
    }
}
