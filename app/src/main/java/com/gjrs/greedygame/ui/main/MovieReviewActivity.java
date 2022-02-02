package com.gjrs.greedygame.ui.main;

import static com.gjrs.greedygame.util.Constants.KEY_MOVIE_ID;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gjrs.greedygame.R;
import com.gjrs.greedygame.model.Movie;
import com.gjrs.greedygame.model.Reviews;
import com.gjrs.greedygame.ui.detail.adapters.ReviewsAdapter;
import com.gjrs.greedygame.ui.detail.async.MovieDetailsAsyncTask;
import com.gjrs.greedygame.ui.detail.async.MovieDetailsCallBack;
import com.gjrs.greedygame.ui.detail.viewmodel.DetailViewModel;
import com.gjrs.greedygame.ui.detail.viewmodel.DetailViewModelFactory;
import com.gjrs.greedygame.util.InjectorUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReviewActivity extends AppCompatActivity implements MovieDetailsCallBack,
         SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.recycler_view_reviews)
    RecyclerView rvReviews;
    @BindView(R.id.tv_no_review)
    TextView tvNoReview;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private DetailViewModel detailViewModel;
    private int mMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review);
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mMovieId = intent.getExtras().getInt(KEY_MOVIE_ID, -1);
        }

        DetailViewModelFactory vDetailViewModelFactory = InjectorUtils.provideDetailViewModelFactory(this, this);
        detailViewModel = ViewModelProviders.of(this, vDetailViewModelFactory).get(DetailViewModel.class);

        new MovieDetailsAsyncTask(detailViewModel, this).execute(mMovieId);

        ivBack.setOnClickListener(view -> finish());
    }


    @Override
    public void complete(final Movie movie) {
        setupReviews(movie.getReviews());
    }

    private void setupReviews(List<Reviews> reviews) {
        if (reviews != null && !reviews.isEmpty()) {
            ReviewsAdapter vReviewsAdapter = new ReviewsAdapter(reviews);
            rvReviews.setHasFixedSize(true);
            rvReviews.setLayoutManager(new LinearLayoutManager(this));
            rvReviews.setAdapter(vReviewsAdapter);
        } else {
            rvReviews.setVisibility(View.GONE);
            tvNoReview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
}