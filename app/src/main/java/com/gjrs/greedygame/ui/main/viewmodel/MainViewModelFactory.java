package com.gjrs.greedygame.ui.main.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gjrs.greedygame.MoviesRepository;

public final class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MoviesRepository mRepository;

    public MainViewModelFactory(MoviesRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked cast")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(mRepository);
    }
}
