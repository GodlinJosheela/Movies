package com.gjrs.greedygame.ui.detail.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.gjrs.greedygame.MoviesRepository;
import com.gjrs.greedygame.model.Movie;
import com.gjrs.greedygame.model.responses.MovieReviewResponse;
import com.gjrs.greedygame.model.responses.MovieTrailerResponse;

public final class DetailViewModel extends ViewModel {
    private MoviesRepository mMoviesRepository;

    DetailViewModel(MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;
    }

    public Movie getMovieDetails(int movieId) {
        return mMoviesRepository
                .getMovieDetails(movieId);
    }

    public MovieTrailerResponse getMovieTrailersResponse(int movieId) {
        return mMoviesRepository
                .getMovieTrailers(movieId);
    }

    public MovieReviewResponse getMovieReviewsResponse(int movieId) {
        return mMoviesRepository
                .getMovieReviews(movieId);
    }

    public void favouriteMovie(Movie movie) {
        mMoviesRepository.favouriteMovieOps(movie);
    }

    public LiveData<Movie> getFav(int movieId) {
        return mMoviesRepository.getSpecificFavouriteMovie(movieId);
    }
}
