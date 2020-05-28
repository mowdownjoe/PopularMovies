package com.android.example.popularmovies.ui;

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
import com.android.example.popularmovies.utils.json.JsonUtils;
import com.android.example.popularmovies.utils.json.MovieJsonException;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {

    //TODO Refactor to use FavMovieClass instead of raw JSON

    @Nullable
    private JSONArray movieData;
    private PosterOnClickListener onClickListener;

    public interface PosterOnClickListener{
        void onListItemClick(JSONObject movieData);
    }

    public PosterAdapter(PosterOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setMovieData(JSONArray movieData) {
        this.movieData = movieData;
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
        if (movieData != null) {
            try {
                holder.bind(JsonUtils.getPosterUri(movieData.getJSONObject(position)));
            } catch (JSONException | MovieJsonException e) {
                Log.e("PosterAdapter.onBind", "Error occurred during binding", e);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (movieData != null) {
            return movieData.length();
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
            if (movieData != null) {
                try {
                    onClickListener.onListItemClick(movieData.getJSONObject(getAdapterPosition()));
                } catch (JSONException e) {
                    Log.e(TAG_ONCLICK, "Cannot find JSON Object in array", e);
                }
            } else {
                Log.w(TAG_ONCLICK, "movieData is null");
            }
        }
    }
}
