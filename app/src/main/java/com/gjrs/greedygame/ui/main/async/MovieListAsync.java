package com.gjrs.greedygame.ui.main.async;

import android.os.AsyncTask;

import java.util.List;

import com.gjrs.greedygame.model.Category;
import com.gjrs.greedygame.model.MovieNetworkLite;
import com.gjrs.greedygame.ui.main.callbacks.MovieListCallBack;
import com.gjrs.greedygame.ui.main.viewmodel.MainViewModel;

public final class MovieListAsync extends AsyncTask<Void, Void, List<MovieNetworkLite>> {
    private MainViewModel mMainViewModel;
    private MovieListCallBack mMovieListCallBack;
    private Category mCategory;
    private int movieId = 0;

    public MovieListAsync(MainViewModel mainViewModel, Category category, MovieListCallBack movieListCallBack) {
        mMainViewModel = mainViewModel;
        mMovieListCallBack = movieListCallBack;
        mCategory = category;
    }

    public MovieListAsync(MainViewModel mainViewModel, Category category, MovieListCallBack movieListCallBack, int movieId) {
        mMainViewModel = mainViewModel;
        mMovieListCallBack = movieListCallBack;
        mCategory = category;
        this.movieId = movieId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mMovieListCallBack.inProgress();
    }

    @Override
    protected List<MovieNetworkLite> doInBackground(Void... voids) {
        if(Category.SIMILAR.equals(mCategory)){
            return mMainViewModel.getMovies(mCategory, 1, movieId);
        }else {
            return mMainViewModel.getMovies(mCategory, 1);
        }
    }

    @Override
    protected void onPostExecute(List<MovieNetworkLite> movies) {
        super.onPostExecute(movies);
        mMovieListCallBack.onFinished(movies, mCategory);
    }
}
