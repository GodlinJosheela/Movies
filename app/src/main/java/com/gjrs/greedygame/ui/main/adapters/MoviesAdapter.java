package com.gjrs.greedygame.ui.main.adapters;

import static com.gjrs.greedygame.util.Constants.POSTER_BASE_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gjrs.greedygame.R;
import com.gjrs.greedygame.model.IMovie;
import com.gjrs.greedygame.model.Movie;
import com.gjrs.greedygame.model.MovieNetworkLite;
import com.gjrs.greedygame.util.PosterImageView;
import com.gjrs.greedygame.util.RoundedCornersTransform;
import com.gjrs.greedygame.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<MovieNetworkLite> mMovieList;
    private IMovieClickHandler mIMovieClickHandler;
    private List<Movie> favouriteMovie;

    public MoviesAdapter(List<MovieNetworkLite> movies,
                         List<Movie> favMovies,
                         IMovieClickHandler IMovieClickHandler) {

        this.mMovieList = movies;
        mIMovieClickHandler = IMovieClickHandler;
        favouriteMovie = favMovies;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        if (mMovieList != null)
            holder.bind(mMovieList.get(position));
        if (favouriteMovie != null)
            holder.bind(favouriteMovie.get(position));
    }

    @Override
    public int getItemCount() {
        if (mMovieList != null)
            return mMovieList.size();
        else
            return favouriteMovie.size();
    }


    public interface IMovieClickHandler {
        void viewMovieDetails(IMovie movie, PosterImageView view, boolean isFavourite);
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {

        IMovie movie;
        @BindView(R.id.iv_poster_image)
        PosterImageView mPosterImage;
        @BindView(R.id.tv_rating_cardlabel)
        TextView mMovieRatings;
        @BindView(R.id.tv_name)
        TextView mMovieName;
        @BindView(R.id.tv_language)
        TextView mMovieLanguage;
        @BindView(R.id.tv_release_date)
        TextView mMovieReleaseDate;

        MoviesViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (mMovieList != null)
                itemView.setOnClickListener(v -> {
                    movie = mMovieList.get(getAdapterPosition());
                    mIMovieClickHandler.viewMovieDetails(movie, mPosterImage, false);
                });
            else
                itemView.setOnClickListener(v -> {
                    movie = favouriteMovie.get(getAdapterPosition());
                    mIMovieClickHandler.viewMovieDetails(movie, mPosterImage, true);
                });

        }

        private void bind(IMovie movie) {
            Picasso.get()
                    .load(POSTER_BASE_URL + movie.getMoviePoster())
                    .error(R.drawable.test)
                    .placeholder(R.drawable.test)
                    .into(mPosterImage);
            String rating = " " + movie.getVoterAverage() + " ";
            mMovieRatings.setText(rating);
            mMovieName.setText(movie.getTitle());
            mMovieLanguage.setText("Language : " + movie.getLanguage());
            mMovieReleaseDate.setText("Release Date : "+ Utils.convertDateFormat(movie.getReleaseDate(),"yyyy-MM-dd", "MMM dd"));
        }
    }
}
