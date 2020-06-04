package com.android.example.popularmovies.ui.main;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.popularmovies.R;
import com.android.example.popularmovies.database.MovieEntry;
import com.android.example.popularmovies.ui.PosterSizes;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {

    //TODO Implement paging using androidx.paging library

    @Nullable
    private List<MovieEntry> movieList;
    private PosterOnClickListener onClickListener;

    public interface PosterOnClickListener{
        void onListItemClick(MovieEntry movie);
    }

    public PosterAdapter(PosterOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setMovieData(List<MovieEntry> movies){
        movieList = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.poster_grid_item, parent, false);

        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder holder, int position) {
        if (movieList != null) {
            holder.bind(movieList.get(position).getPosterUri(PosterSizes.LARGE));
        }
    }

    @Override
    public int getItemCount() {
        if (movieList != null) {
            return movieList.size();
        } else {
            return 0;
        }
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        static final String TAG_ONCLICK = "PosterViewHolderOnClick";
        ImageView posterImage;

        PosterViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.iv_poster_image);
            itemView.setOnClickListener(this);
        }

        void bind(String url){
            Uri uri = Uri.parse(url);
            bind(uri);
        }

        void bind(Uri uri){
            Log.v("PosterViewHolder.bind", "Loading from URI: "+uri.toString());
            Picasso.get()
                    .load(uri)
                    .placeholder(R.drawable.loading_poster)
                    .error(R.drawable.error_poster)
                    .into(posterImage);
        }

        @Override
        public void onClick(View v) {
            Log.v(TAG_ONCLICK, "Received click on item "+getAdapterPosition());
            if (movieList != null) {
                onClickListener.onListItemClick(movieList.get(getAdapterPosition()));
            } else {
                Log.w(TAG_ONCLICK, "movieData is null");
            }
        }
    }
}
