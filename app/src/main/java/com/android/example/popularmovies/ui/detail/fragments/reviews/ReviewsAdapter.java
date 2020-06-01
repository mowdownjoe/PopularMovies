package com.android.example.popularmovies.ui.detail.fragments.reviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.popularmovies.R;
import com.android.example.popularmovies.databinding.ReviewListItemBinding;
import com.android.example.popularmovies.utils.json.MovieReview;

import java.util.List;

import io.noties.markwon.Markwon;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    @Nullable
    private List<MovieReview> mReviews;
    private Context context;
    private Markwon markwon;

    public interface ReviewOnClickListener{
        void onListItemClick(MovieReview review);
    }

    private ReviewOnClickListener onClickListener;

    public ReviewsAdapter(ReviewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        context = null;
    }

    public ReviewsAdapter(Context c, ReviewOnClickListener listener){
        onClickListener = listener;
        context = c;
        markwon = Markwon.create(c);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.review_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        if (mReviews != null) {
            holder.bind(mReviews.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (mReviews != null) {
            return mReviews.size();
        } else {
            return 0;
        }
    }

    public void setReviews(List<MovieReview> reviews){
        mReviews = reviews;
        notifyDataSetChanged();
    }

    public void setupMarkdownParsing(Context c){
        if (context == null || !context.equals(c)){
            context = c;
        }
        markwon = Markwon.create(context);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ReviewListItemBinding binding;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ReviewListItemBinding.bind(itemView);
            itemView.setOnClickListener(this);
        }

        void bind(MovieReview review){
            binding.tvAuthor.setText(review.getAuthor());
            markwon.setMarkdown(binding.tvReview, review.getContent());
        }

        @Override
        public void onClick(View v) {
            if (mReviews != null){
                onClickListener.onListItemClick(mReviews.get(getAdapterPosition()));
            }
        }
    }
}
