package com.gjrs.greedygame.ui.detail.adapters;

import static com.gjrs.greedygame.util.Constants.POSTER_BASE_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.gjrs.greedygame.R;
import com.gjrs.greedygame.model.Reviews;
import com.gjrs.greedygame.util.Utils;
import com.squareup.picasso.Picasso;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MoviesReviewViewHolder> {

    private List<Reviews> mReviewsList;

    public ReviewsAdapter(List<Reviews> moviesReview) {
        mReviewsList = moviesReview;
    }

    @NonNull
    @Override
    public MoviesReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MoviesReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesReviewViewHolder holder, int position) {
        holder.bind(mReviewsList.get(position));
    }

    @Override
    public int getItemCount() {
        return (mReviewsList == null) ? 0 : mReviewsList.size();
    }

    public static class MoviesReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_reviews_author)
        TextView tvAuthor;
        @BindView(R.id.tv_reviews_date)
        TextView tvDate;
        @BindView(R.id.tv_reviews_rating)
        TextView tvRating;
        @BindView(R.id.iv_author_avatar)
        ImageView ivAuthorAvatar;
        @BindView(R.id.tv_reviews_content)
        TextView tvContent;
        @BindView(R.id.rating_bar_movie_avg)
        RatingBar rbMovieRating;

        MoviesReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Reviews movieReviews) {
            if (movieReviews != null) {
                String author = movieReviews.getAuthor();
                String reviews = movieReviews.getContent();
                tvAuthor.setText(author);
                tvContent.setText(reviews);
                tvRating.setText(movieReviews.getAuthorDetails().getRating());
                tvDate.setText(Utils.convertDateFormat(movieReviews.getDate(),"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'","dd-MM-yyyy"));
                Picasso.get()
                        .load(POSTER_BASE_URL + movieReviews.getAuthorDetails().getAvatarPath())
                        .error(R.drawable.test)
                        .placeholder(R.drawable.test)
                        .into(ivAuthorAvatar);
                if(movieReviews.getAuthorDetails().getRating() == null)
                rbMovieRating.setRating(0);
                try {
                    rbMovieRating.setRating(Float.parseFloat(movieReviews.getAuthorDetails().getRating()) / 2);
                }catch (Exception e){
                    rbMovieRating.setRating(0);
                }
            }
        }
    }
}
