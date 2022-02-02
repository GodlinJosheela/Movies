package com.gjrs.greedygame.ui.main.menu;

import android.view.MenuItem;

import androidx.appcompat.widget.PopupMenu;

import com.gjrs.greedygame.R;
import com.gjrs.greedygame.model.Category;
import com.gjrs.greedygame.ui.main.async.MovieListAsync;
import com.gjrs.greedygame.ui.main.callbacks.FavouriteMoviesCallback;
import com.gjrs.greedygame.ui.main.callbacks.MovieListCallBack;
import com.gjrs.greedygame.ui.main.viewmodel.MainViewModel;

public class CategoryMenuListener implements PopupMenu.OnMenuItemClickListener {

    private MovieListCallBack mMovieListCallBackContext;
    private FavouriteMoviesCallback mFavouriteMoviesCallbackContext;
    private MainViewModel mMainViewModel;

    public CategoryMenuListener(MainViewModel mainViewModel, MovieListCallBack movieListCallBackContext, FavouriteMoviesCallback favouriteMoviesCallbackContext) {
        this.mMovieListCallBackContext = movieListCallBackContext;
        this.mMainViewModel = mainViewModel;
        this.mFavouriteMoviesCallbackContext = favouriteMoviesCallbackContext;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_top_rated:
                new MovieListAsync(mMainViewModel, Category.TOP_RATED, mMovieListCallBackContext).execute();
                return true;
            case R.id.action_sort_upcoming:
                new MovieListAsync(mMainViewModel, Category.UPCOMING, mMovieListCallBackContext).execute();
                return true;
            case R.id.action_sort_most_popular:
                new MovieListAsync(mMainViewModel, Category.POPULAR, mMovieListCallBackContext).execute();
                return true;
            case R.id.action_sort_favourites:
                mFavouriteMoviesCallbackContext.loadFavouriteMovies();
                return true;
        }
        return false;
    }
}
