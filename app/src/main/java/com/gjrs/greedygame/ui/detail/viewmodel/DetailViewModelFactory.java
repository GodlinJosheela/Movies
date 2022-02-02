package com.gjrs.greedygame.ui.detail.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gjrs.greedygame.MoviesRepository;

public final class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MoviesRepository mRepository;

    public DetailViewModelFactory(MoviesRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked cast")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailViewModel(mRepository);
    }
}
