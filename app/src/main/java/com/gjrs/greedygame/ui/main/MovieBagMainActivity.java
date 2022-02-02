package com.gjrs.greedygame.ui.main;


import static android.content.ContentValues.TAG;
import static com.gjrs.greedygame.util.Constants.BACKDROP_BASE_URL;
import static com.gjrs.greedygame.util.Constants.KEY_MOVIE_ID;
import static com.gjrs.greedygame.util.Constants.KEY_MOVIE_IS_FAVOURITE;
import static com.gjrs.greedygame.util.Constants.KEY_MOVIE_POSTER;
import static com.gjrs.greedygame.util.Constants.POSTER_BASE_URL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.gjrs.greedygame.R;
import com.gjrs.greedygame.model.Category;
import com.gjrs.greedygame.model.IMovie;
import com.gjrs.greedygame.model.Movie;
import com.gjrs.greedygame.model.MovieNetworkLite;
import com.gjrs.greedygame.ui.detail.MovieDetailActivity;
import com.gjrs.greedygame.ui.detail.adapters.CustomPagerAdapter;
import com.gjrs.greedygame.ui.main.adapters.MoviesAdapter;
import com.gjrs.greedygame.ui.main.async.MovieListAsync;
import com.gjrs.greedygame.ui.main.callbacks.FavouriteMoviesCallback;
import com.gjrs.greedygame.ui.main.callbacks.MovieListCallBack;
import com.gjrs.greedygame.ui.main.menu.CategoryMenuListener;
import com.gjrs.greedygame.ui.main.viewmodel.MainViewModel;
import com.gjrs.greedygame.ui.main.viewmodel.MainViewModelFactory;
import com.gjrs.greedygame.ui.settings.SettingsActivity;
import com.gjrs.greedygame.util.InjectorUtils;
import com.gjrs.greedygame.util.NetworkUtils;
import com.gjrs.greedygame.util.PosterImageView;
import com.gjrs.greedygame.util.ViewUtils;

public class MovieBagMainActivity extends AppCompatActivity implements MovieListCallBack, FavouriteMoviesCallback,
        MoviesAdapter.IMovieClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {

    private final String KEY_APPBAR_TITLE_PERSISTENCE = "movie_category";
    private final String KEY_MOVIE_LIST_PERSISTENCE = "movie_list";
    private final String KEY_FAV_MOVIE_LIST_PERSISTENCE = "fav_movie_list";
    @BindView(R.id.progressbar_movies_loading)
    ProgressBar pbLoadMovies;
    @BindView(R.id.recycler_view_movies)
    RecyclerView rvMovies;
    @BindView(R.id.text_view_info_message)
    TextView tvInfoMessage;
    @BindView(R.id.text_view_favourites_message)
    TextView tvFavMessage;
    @BindView(R.id.adv_pager)
    ViewPager mViewPager;
    @BindView(R.id.home_layoutDots)
    LinearLayout dotsLayout;
    private List<MovieNetworkLite> mMovieList;
    private List<MovieNetworkLite> mNowPlayingMovieList;
    private List<Movie> favouriteMovies;
    private MainViewModel mMainViewModel;
    private Timer timer;
    private int timeDelay = 10000;
    private List<String> bannerList;

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // page scrolled not required to check
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // page scrolled not required to check
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        MainViewModelFactory vMainViewModelFactory = InjectorUtils.provideMainViewModelFactory(this, this);
        mMainViewModel = ViewModelProviders.of(this, vMainViewModelFactory).get(MainViewModel.class);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_MOVIE_LIST_PERSISTENCE)) {
            mMovieList = Parcels.unwrap(savedInstanceState.getParcelable(KEY_MOVIE_LIST_PERSISTENCE));
            setTitle(savedInstanceState.getCharSequence(KEY_APPBAR_TITLE_PERSISTENCE));
            bindNetworkMovies(mMovieList);
        } else if (savedInstanceState != null && savedInstanceState.containsKey(KEY_FAV_MOVIE_LIST_PERSISTENCE)) {
            favouriteMovies = Parcels.unwrap(savedInstanceState.getParcelable(KEY_FAV_MOVIE_LIST_PERSISTENCE));
            setTitle(savedInstanceState.getCharSequence(KEY_APPBAR_TITLE_PERSISTENCE));
            bindFavouriteMovies(favouriteMovies);
        } else if (NetworkUtils.isOnline(this)) {
            removeMessageInfo(tvInfoMessage);
            new MovieListAsync(mMainViewModel, Category.NOW_PLAYING, this).execute();
            new MovieListAsync(mMainViewModel, Category.POPULAR, this).execute();
        }
    }

    private void bindFavouriteMovies(List<Movie> favouriteMovies) {
        if (favouriteMovies.isEmpty()) {
            removeMessageInfo(tvInfoMessage);
            showDefaultFavMessage();
        } else {
            MoviesAdapter vMoviesAdapter = new MoviesAdapter(null, favouriteMovies, this);
            setupRecyclerView(vMoviesAdapter);
        }
    }

    private void setupRecyclerView(MoviesAdapter moviesAdapter) {
        ViewUtils.setupRecyclerView(rvMovies, new LinearLayoutManager(getApplicationContext()), this);
        rvMovies.setAdapter(moviesAdapter);
        pbLoadMovies.setVisibility(View.GONE);
    }

    private void removeMessageInfo(TextView tvInfoMessage) {
        if (tvInfoMessage.getVisibility() == View.VISIBLE)
            tvInfoMessage.setVisibility(View.GONE);
    }

    @Override
    public void loadFavouriteMovies() {
        mMainViewModel.getFavouriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMovieList = null;
                if (movies != null) {
                    favouriteMovies = movies;
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setTitle(getString(R.string.action_sort_favourites));
                    bindFavouriteMovies(favouriteMovies);
                }
            }
        });
    }

    private void setAppBarTitle(Category category) {
        if (getSupportActionBar() != null)
            switch (category) {
                case TOP_RATED:
                    getSupportActionBar().setTitle(getString(R.string.action_sort_top_rated));
                    break;
                case UPCOMING:
                    getSupportActionBar().setTitle(getString(R.string.action_sort_upcoming));
                    break;
                case POPULAR:
                    getSupportActionBar().setTitle(getString(R.string.action_sort_most_popular));
                    break;
            }
    }

    void bindNetworkMovies(List<MovieNetworkLite> movies) {
        if (movies != null) {
            MoviesAdapter vMoviesAdapter = new MoviesAdapter(movies, null, this);
            setupRecyclerView(vMoviesAdapter);
        } else {
            showNoConnectionMessage();
        }
    }

    private void showNoConnectionMessage() {
        rvMovies.setVisibility(View.GONE);
        tvInfoMessage.setVisibility(View.VISIBLE);
        pbLoadMovies.setVisibility(View.GONE);
    }

    private void showDefaultFavMessage() {
        rvMovies.setVisibility(View.GONE);
        pbLoadMovies.setVisibility(View.GONE);
        tvFavMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMovieList != null)
            outState.putParcelable(KEY_MOVIE_LIST_PERSISTENCE, Parcels.wrap(mMovieList));
        if (favouriteMovies != null)
            outState.putParcelable(KEY_FAV_MOVIE_LIST_PERSISTENCE, Parcels.wrap(favouriteMovies));
        if (getSupportActionBar() != null)
            outState.putCharSequence(KEY_APPBAR_TITLE_PERSISTENCE, getSupportActionBar().getTitle());
    }

    @Override
    public void inProgress() {
        removeMessageInfo(tvFavMessage);
        pbLoadMovies.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinished(List<MovieNetworkLite> movies, Category category) {
        setAppBarTitle(category);
        if(Category.POPULAR.equals(category)) {
            mMovieList = movies;
            bindNetworkMovies(mMovieList);
        }else if(Category.NOW_PLAYING.equals(category)){
            mNowPlayingMovieList = movies;
            bannerList = new ArrayList<>();
            for(MovieNetworkLite model : movies){
                bannerList.add(BACKDROP_BASE_URL + model.getMoviePoster());
            }

            loadPager();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_sort) {
            showSortPopUpMenu();
            return true;
        } else if (itemId == R.id.action_settings) {
            Intent settingsIntent = new Intent(MovieBagMainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortPopUpMenu() {
        PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.action_sort));
        sortMenu.setOnMenuItemClickListener(new CategoryMenuListener(mMainViewModel, this, this));
        sortMenu.inflate(R.menu.category_menu);
        sortMenu.show();
    }

    @Override
    public void viewMovieDetails(IMovie movie, PosterImageView view, boolean isFavourite) {
        Intent vIntent = new Intent(this, MovieDetailActivity.class);
        vIntent.putExtra(KEY_MOVIE_ID, movie.getMovieId());
        vIntent.putExtra(KEY_MOVIE_POSTER, movie.getMoviePoster());
        vIntent.putExtra(KEY_MOVIE_IS_FAVOURITE, isFavourite);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(view));
        startActivity(vIntent, options.toBundle());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_language_key))) {
//            FIXME Change Language of request
            String language = sharedPreferences.getString(key, getResources().getString(R.string.pref_language_val_english));
            Language.setUpLocale(language, this);
            new MovieListAsync(mMainViewModel, Category.UPCOMING, this).execute();
        }
    }


    private void loadPager() {
        if (!bannerList.isEmpty()) {
            try {
                int bannerLimit = 30;
                if (bannerLimit > 0) {
                    timeDelay = bannerLimit * 1000;
                } else {
                    timeDelay = 10 * 1000;
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this, bannerList);

            mViewPager.setAdapter(mCustomPagerAdapter);
            mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    mViewPager.post(() -> mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1) % bannerList.size()));
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, timeDelay, timeDelay);
            addBottomDots(0);
        }
    }

    private void addBottomDots(int currentPage) {
        try {
            TextView[] textDots = new TextView[bannerList.size()];
            dotsLayout.removeAllViews();
            for (int index = 0; index < textDots.length; index++) {
                textDots[index] = new TextView(this);
                textDots[index].setText(fromHtml("&#8226;"));
                textDots[index].setTextSize(35);
                textDots[index].setTextColor(ContextCompat.getColor(this, R.color.grey_dot));
                dotsLayout.addView(textDots[index]);
            }
            if (textDots.length > 0)
                textDots[currentPage].setTextColor(ContextCompat.getColor(this, R.color.blue_400));
        } catch (Exception e) {
            Log.e(TAG, "addBottomDots: " + e.getMessage(), e);
        }
    }

    public static Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
