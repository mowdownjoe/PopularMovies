package com.android.example.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.popularmovies.utils.JsonUtils;
import com.android.example.popularmovies.utils.MovieJsonException;
import com.squareup.picasso.Picasso;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {

    public void setMovieData(JSONArray movieData) {
        this.movieData = movieData;
    }

    private JSONArray movieData;



    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.poster_grid_item, parent, false);

        //TODO Create viewHolder and return
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder holder, int position) {
        try {
            holder.bind(JsonUtils.getPosterUri(movieData.getJSONObject(position)));
        } catch (JSONException | MovieJsonException e) {
            Log.e("PosterAdapter.onBind", "Error occurred during binding", e);
        }
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
            bind(uri);
        }

        void bind(Uri uri){
            //TODO Add Placeholder and Error arguments to Picasso train, and related drawables.
            Picasso.get().load(uri).into(posterImage);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
