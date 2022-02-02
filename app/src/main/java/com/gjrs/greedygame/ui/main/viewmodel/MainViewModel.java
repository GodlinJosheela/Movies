package com.gjrs.greedygame.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import com.gjrs.greedygame.MoviesRepository;
import com.gjrs.greedygame.model.Category;
import com.gjrs.greedygame.model.Movie;
import com.gjrs.greedygame.model.MovieNetworkLite;
import com.gjrs.greedygame.model.responses.MovieListResponse;

public final class MainViewModel extends ViewModel {
    private MoviesRepository mMoviesRepository;

    MainViewModel(MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;
    }

    public List<MovieNetworkLite> getMovies(Category category, int page) {
        MovieListResponse vMovieListResponse = mMoviesRepository
                .getMovies(category, page);
        return page <= vMovieListResponse.getTotalPages() ? vMovieListResponse.getMoviesResult() : null;
    }
    public List<MovieNetworkLite> getMovies(Category category, int page, int movieId) {
        MovieListResponse vMovieListResponse = mMoviesRepository
                .getMovies(category, page, movieId);
        return page <= vMovieListResponse.getTotalPages() ? vMovieListResponse.getMoviesResult() : null;
    }

    public LiveData<List<Movie>> getFavouriteMovies() {
        return mMoviesRepository.getFavouriteMovies();
    }
}
