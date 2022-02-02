package com.gjrs.greedygame.ui.detail.adapters;

import static com.gjrs.greedygame.util.Constants.KEY_MOVIE_ID;
import static com.gjrs.greedygame.util.Constants.KEY_MOVIE_IS_FAVOURITE;
import static com.gjrs.greedygame.util.Constants.KEY_MOVIE_POSTER;
import static com.gjrs.greedygame.util.Constants.POSTER_BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gjrs.greedygame.model.MovieNetworkLite;
import com.gjrs.greedygame.ui.detail.MovieDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.gjrs.greedygame.R;
import com.gjrs.greedygame.model.Trailers;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.MoviesVideoViewHolder> {

    private List<MovieNetworkLite> mTrailersList;
    private static IWatchTrailerClickHandler sMIWatchTrailerClickHandler;
    private static IShareTrailerHandler sMIShareTrailerHandler;
    Context context;

    public TrailersAdapter(List<MovieNetworkLite> mMovieTrailerList, IWatchTrailerClickHandler iWatchTrailerClickHandler, IShareTrailerHandler iShareTrailerHandler) {
        mTrailersList = mMovieTrailerList;
        sMIWatchTrailerClickHandler = iWatchTrailerClickHandler;
        sMIShareTrailerHandler = iShareTrailerHandler;
    }

    @NonNull
    @Override
    public MoviesVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         context = parent.getContext();
        int layoutIdForListItem = R.layout.similar_movies_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MoviesVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesVideoViewHolder holder, int position) {
        holder.bind(mTrailersList.get(position));
    }

    @Override
    public int getItemCount() {
        return (mTrailersList == null) ? 0 : mTrailersList.size();
    }

    public class MoviesVideoViewHolder extends RecyclerView.ViewHolder {

        private String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";
        MovieNetworkLite mTrailers;
        @BindView(R.id.iv_trailer)
        ImageView mMovieTrailerThumbnailImageView;

        MoviesVideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mMovieTrailerThumbnailImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(KEY_MOVIE_ID, mTrailers.getMovieId());
                    intent.putExtra(KEY_MOVIE_POSTER, mTrailers.getMoviePoster());
                    intent.putExtra(KEY_MOVIE_IS_FAVOURITE, false);
                    context.startActivity(intent);
                }
            });
        }

        private void bind(MovieNetworkLite trailers) {
            mTrailers = trailers;
            Picasso.get()
                    .load(POSTER_BASE_URL+ trailers.getMoviePoster())
                    .placeholder(R.color.colorAlternate)
                    .into(mMovieTrailerThumbnailImageView);
        }
    }

}
