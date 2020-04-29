package com.android.example.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import com.squareup.picasso.Picasso;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {

    private String[] posterUrls;

    public void setPosterUrls(String[] posterUrls) {
        this.posterUrls = posterUrls;
    }

    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.poster_grid_item, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder holder, int position) {
        holder.bind(posterUrls[position]);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView posterImage;

        public PosterViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.iv_poster_image);
        }

        void bind(String url){
            Uri uri = Uri.parse(url);
            //TODO Add Placeholder and Error arguments to Picasso train, and related drawables.
            Picasso.get().load(uri).into(posterImage);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
