package com.gjrs.greedygame.ui.main.callbacks;

import java.util.List;

import com.gjrs.greedygame.model.Category;
import com.gjrs.greedygame.model.MovieNetworkLite;

public interface MovieListCallBack {

    void inProgress();

    void onFinished(List<MovieNetworkLite> movies, Category category);
}
