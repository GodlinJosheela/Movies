package com.gjrs.greedygame;

import android.util.Log;

import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.util.List;

import com.gjrs.greedygame.db.dao.IFavouriteMovieDao;
import com.gjrs.greedygame.model.Category;
import com.gjrs.greedygame.model.Movie;
import com.gjrs.greedygame.BuildConfig;
import com.gjrs.greedygame.model.responses.MovieListResponse;
import com.gjrs.greedygame.model.responses.MovieReviewResponse;
import com.gjrs.greedygame.model.responses.MovieTrailerResponse;
import com.gjrs.greedygame.network.MovieApiServices;
import com.gjrs.greedygame.util.threads.AppExecutors;

public final class MoviesRepository {

    private static MoviesRepository sMoviesRepository;
    private MovieApiServices mMovieApiServices;
    private IFavouriteMovieDao mMovieDao;
    private AppExecutors mThreadAppExecutors;
    private static final Object LOCK = new Object();
    private MovieListResponse mMovieListResponse;
    private Movie mMovie;
    private final String TAG = MoviesRepository.class.getSimpleName();
    private MovieReviewResponse mMovieReviewResponse;
    private MovieTrailerResponse mMMovieTrailerResponse;
    private String language;


    private MoviesRepository(MovieApiServices movieApiServices, IFavouriteMovieDao movieDao, AppExecutors threadAppExecutors, String language) {
        mMovieApiServices = movieApiServices;
        mMovieDao = movieDao;
        mThreadAppExecutors = threadAppExecutors;
        this.language = language;
    }

    public synchronized static MoviesRepository getInstance(IFavouriteMovieDao movieDao, MovieApiServices movieApiServices, AppExecutors threadAppExecutors, String language) {
        if (sMoviesRepository == null) {
            synchronized (LOCK) {
                sMoviesRepository = new MoviesRepository(movieApiServices, movieDao, threadAppExecutors, language);
            }
        }
        return sMoviesRepository;
    }

    public LiveData<List<Movie>> getFavouriteMovies() {
        return mMovieDao.loadAll();
    }

    public LiveData<Movie> getSpecificFavouriteMovie(int id) {
        return mMovieDao.loadById(id);
    }

    public void favouriteMovieOps(final Movie movie) {
        mThreadAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mMovieDao.checkForFavs(movie.getMovieId(), movie.getMovieTitle()) != null) {
                    mMovieDao.removeFromFavourites(movie);
                    Log.d(TAG, "Movie Deleted");
                } else {
                    mMovieDao.addToFavourites(movie);
                    Log.d(TAG, "Movie Saved");
                }
            }
        });
    }

    public MovieListResponse getMovies(Category category, final int page) {
        switch (category) {
            case POPULAR:
                loadPopularMovies(page);
                break;
            case UPCOMING:
                loadUpcomingMovies(page);
                break;
            case TOP_RATED:
                loadTopRatedMovies(page);
                break;
            case NOW_PLAYING:
                loadNowPlayingMovies(page);
                break;
        }
        return mMovieListResponse != null ? mMovieListResponse : new MovieListResponse();
    }
    public MovieListResponse getMovies(Category category, final int page, int movieId) {
        switch (category) {
            case SIMILAR:
                loadSimilarMovies(page, movieId);
                break;
        }
        return mMovieListResponse != null ? mMovieListResponse : new MovieListResponse();
    }

    private void loadTopRatedMovies(int page) {
        try {
            mMovieListResponse = mMovieApiServices
                    .getTopRatedMovies(BuildConfig.API_KEY, language, page)
                    .execute()
                    .body();
            Log.d(TAG, "Upcoming Movies Loaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void loadNowPlayingMovies(int page) {
        try {
            mMovieListResponse = mMovieApiServices
                    .getNowPlayingMovies(BuildConfig.API_KEY, language, page)
                    .execute()
                    .body();
            Log.d(TAG, "Now Playing Movies Loaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void loadSimilarMovies(int page, int movieId) {
        try {
            mMovieListResponse = mMovieApiServices
                    .getSimilarMovies(movieId,BuildConfig.API_KEY, language, page)
                    .execute()
                    .body();
            Log.d(TAG, "Now Playing Movies Loaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void loadUpcomingMovies(int page) {
        try {
            mMovieListResponse = mMovieApiServices
                    .getUpcomingMovies(BuildConfig.API_KEY, language, page)
                    .execute()
                    .body();
            Log.d(TAG, "Top Rated Movies Loaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void loadPopularMovies(int page) {
        try {
            mMovieListResponse = mMovieApiServices
                    .getPopularMovies(BuildConfig.API_KEY, language, page)
                    .execute()
                    .body();
            Log.d(TAG, "Popular Movies Loaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public Movie getMovieDetails(final int movieId) {
        try {
            mMovie = mMovieApiServices
                    .getMovie(movieId, BuildConfig.API_KEY, language)
                    .execute()
                    .body();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return mMovie != null ? mMovie : getDefaultMovie();
    }

    private Movie getDefaultMovie() {
        mMovie = new Movie();
        return mMovie;
    }

    public MovieReviewResponse getMovieReviews(final int movieId) {
        try {
            mMovieReviewResponse = mMovieApiServices
                    .getMovieReviews(movieId, BuildConfig.API_KEY, language)
                    .execute()
                    .body();
            Log.e(TAG, "Reviews Loaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return mMovieReviewResponse != null ? mMovieReviewResponse : new MovieReviewResponse();
    }

    public MovieTrailerResponse getMovieTrailers(final int movieId) {
        try {
            mMMovieTrailerResponse = mMovieApiServices
                    .getMovieTrailers(movieId, BuildConfig.API_KEY, language)
                    .execute()
                    .body();
            Log.e(TAG, "Trailers Loaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return mMMovieTrailerResponse != null ? mMMovieTrailerResponse : new MovieTrailerResponse();
    }

}
