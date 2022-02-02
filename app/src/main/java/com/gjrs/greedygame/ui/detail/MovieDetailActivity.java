package com.gjrs.greedygame.ui.detail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gjrs.greedygame.model.Category;
import com.gjrs.greedygame.model.MovieNetworkLite;
import com.gjrs.greedygame.ui.main.MovieReviewActivity;
import com.gjrs.greedygame.ui.main.adapters.MoviesAdapter;
import com.gjrs.greedygame.ui.main.async.MovieListAsync;
import com.gjrs.greedygame.ui.main.callbacks.MovieListCallBack;
import com.gjrs.greedygame.ui.main.viewmodel.MainViewModel;
import com.gjrs.greedygame.ui.main.viewmodel.MainViewModelFactory;
import com.gjrs.greedygame.util.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.gjrs.greedygame.R;
import com.gjrs.greedygame.model.Movie;
import com.gjrs.greedygame.model.Reviews;
import com.gjrs.greedygame.model.Trailers;
import com.gjrs.greedygame.ui.detail.adapters.IShareTrailerHandler;
import com.gjrs.greedygame.ui.detail.adapters.IWatchTrailerClickHandler;
import com.gjrs.greedygame.ui.detail.adapters.ReviewsAdapter;
import com.gjrs.greedygame.ui.detail.adapters.TrailersAdapter;
import com.gjrs.greedygame.ui.detail.async.MovieDetailsAsyncTask;
import com.gjrs.greedygame.ui.detail.async.MovieDetailsCallBack;
import com.gjrs.greedygame.ui.detail.viewmodel.DetailViewModel;
import com.gjrs.greedygame.ui.detail.viewmodel.DetailViewModelFactory;
import com.gjrs.greedygame.util.InjectorUtils;
import com.gjrs.greedygame.util.BackdropImageView;
import com.gjrs.greedygame.util.PosterImageView;
import com.gjrs.greedygame.util.threads.AppExecutors;

import static com.gjrs.greedygame.util.Constants.BACKDROP_BASE_URL;
import static com.gjrs.greedygame.util.Constants.KEY_MOVIE_ID;
import static com.gjrs.greedygame.util.Constants.KEY_MOVIE_IS_FAVOURITE;
import static com.gjrs.greedygame.util.Constants.KEY_MOVIE_POSTER;
import static com.gjrs.greedygame.util.Constants.POSTER_BASE_URL;
import static com.gjrs.greedygame.util.PaletteExtractorUtil.getBitmapFromUrl;
import static com.gjrs.greedygame.util.PaletteExtractorUtil.getDarkVibrantColor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailsCallBack,
        SharedPreferences.OnSharedPreferenceChangeListener, MovieListCallBack {
    private final String KEY_MOVIE_PERSISTENCE = "movie";
    @BindView(R.id.iv_poster_image_details)
    PosterImageView vPosterImageView;
    @BindView(R.id.backdrop_image_view)
    BackdropImageView mBackdropImageView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.text_view_movie_title)
    TextView tvMovieTitle;
    @BindView(R.id.rating_bar_movie_avg)
    RatingBar rbMovieRating;
    @BindView(R.id.text_view_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.text_view_overview)
    TextView tvOverview;
    @BindView(R.id.text_view_language)
    TextView tvLanguage;
    @BindView(R.id.text_view_release_status)
    TextView tvReleaseStatus;
    @BindView(R.id.recycler_view_reviews)
    RecyclerView rvReviews;
    @BindView(R.id.recycler_view_similar)
    RecyclerView rvSimilarMovies;
    @BindView(R.id.tv_no_review)
    TextView tvNoReview;
    @BindView(R.id.tv_no_trailer)
    TextView tvNoTrailer;
    @BindView(R.id.image_view_favourites)
    ImageView ivFavourites;
    @BindView(R.id.btn_cast)
    Button btnCast;
    @BindView(R.id.btn_review)
    Button btnReview;

    private int mMovieId;
    private String mMoviePoster;
    private Movie mMovie;
    private DetailViewModel detailViewModel;
    private boolean mIsFavourite;
    private MainViewModel mMainViewModel;
    private List<MovieNetworkLite> mMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        supportPostponeEnterTransition();
        vPosterImageView.setTransitionName("poster");
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mMovieId = intent.getExtras().getInt(KEY_MOVIE_ID, -1);
            mMoviePoster = intent.getStringExtra(KEY_MOVIE_POSTER);
            mIsFavourite = intent.getBooleanExtra(KEY_MOVIE_IS_FAVOURITE, false);
        }
        posterImageTransition();
        DetailViewModelFactory vDetailViewModelFactory = InjectorUtils.provideDetailViewModelFactory(this, this);
        detailViewModel = ViewModelProviders.of(this, vDetailViewModelFactory).get(DetailViewModel.class);

        MainViewModelFactory vMainViewModelFactory = InjectorUtils.provideMainViewModelFactory(this, this);
        mMainViewModel = ViewModelProviders.of(this, vMainViewModelFactory).get(MainViewModel.class);

        checkIfFavourite(mMovieId);
        if (savedInstanceState != null) {
            mMovie = Parcels.unwrap(savedInstanceState.getParcelable(KEY_MOVIE_PERSISTENCE));
            complete(mMovie);
        } else {
            if (!mIsFavourite) {
                new MovieDetailsAsyncTask(detailViewModel, this).execute(mMovieId);
            } else {
                loadMovieDetailsFromCache();
            }
        }

        ivBack.setOnClickListener(view -> finish());
        btnReview.setOnClickListener(view -> {
            Intent vIntent = new Intent(MovieDetailActivity.this, MovieReviewActivity.class);
            vIntent.putExtra(KEY_MOVIE_ID, mMovieId);
            startActivity(vIntent);
        });

        new MovieListAsync(mMainViewModel, Category.SIMILAR, this, mMovieId).execute();

    }

    private void loadMovieDetailsFromCache() {
        detailViewModel.getFav(mMovieId).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null) {
                    mMovie = movie;
                    complete(mMovie);
                }
            }
        });
    }

    private void checkIfFavourite(int movieId) {
        detailViewModel.getFav(movieId).observe(this, movie -> {
            if (movie != null) {
                ivFavourites.setImageDrawable(getDrawable(R.drawable.ic_favorite_true));
            } else {
                ivFavourites.setImageDrawable(getDrawable(R.drawable.ic_favorite_false));
            }
        });
    }

    private void posterImageTransition() {
        Picasso.get()
                .load(POSTER_BASE_URL + mMoviePoster)
                .into(vPosterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError(Exception e) {
                        supportStartPostponedEnterTransition();
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMovie != null)
            outState.putParcelable(KEY_MOVIE_PERSISTENCE, Parcels.wrap(mMovie));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupTrailers(List<MovieNetworkLite> trailers) {
        if (trailers != null && !trailers.isEmpty()) {
            TrailersAdapter vTrailersAdapter = new TrailersAdapter(trailers, new IWatchTrailerClickHandler() {
                @Override
                public void onClick(Trailers trailers) {
                    TrailerIntentHandler.viewTrailer(trailers, MovieDetailActivity.this);
                }
            }, new IShareTrailerHandler() {
                @Override
                public void onClick(Trailers trailers) {
                    TrailerIntentHandler.shareTrailerLink(trailers, MovieDetailActivity.this);
                }
            });
            rvSimilarMovies.setHasFixedSize(true);
            rvSimilarMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvSimilarMovies.setAdapter(vTrailersAdapter);
        } else {
            rvSimilarMovies.setVisibility(View.GONE);
            tvNoTrailer.setVisibility(View.VISIBLE);
        }
    }

    public void setStatusBarColorFromBackdrop(final String url) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                final Bitmap sBitmap = getBitmapFromUrl(url);
                runOnUiThread(() -> {
                    int backGroundColor = ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark);
                    if (getDarkVibrantColor(sBitmap) != null) {
                        backGroundColor = getDarkVibrantColor(sBitmap).getRgb();
                    }
                    getWindow().setStatusBarColor(backGroundColor);
                });
            }
        });
    }

    @Override
    public void complete(final Movie movie) {
        setStatusBarColorFromBackdrop(BACKDROP_BASE_URL + movie.getBackdrop());
        Picasso.get()
                .load(BACKDROP_BASE_URL + movie.getBackdrop())
                .into(mBackdropImageView);
        tvMovieTitle.setText(movie.getMovieTitle());
        rbMovieRating.setRating(movie.getVoterAverage() / 2);
        tvReleaseDate.setText("Release Date : "+ Utils.convertDateFormat(movie.getMovieReleaseDate(),"yyyy-MM-dd", "MMM dd"));
        tvLanguage.setText("Language : "+movie.getLanguage());
        if(Utils.compareDates(movie.getReleaseDate(),Utils.getCurrentDate())){
            tvReleaseStatus.setText("Release Status : Released");
        }else {
            tvReleaseStatus.setText("Release Status : Not Released");
        }
        tvOverview.setText(movie.getMovieOverview());

        mMovie = movie;
        ivFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailViewModel.favouriteMovie(movie);
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void inProgress() {

    }

    @Override
    public void onFinished(List<MovieNetworkLite> movies, Category category) {
        mMovieList = movies;
        setupTrailers(movies);
    }
}
