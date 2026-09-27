package com.example.agrosmart.presentation.viewmodels.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.agrosmart.domain.usecase.CropsUseCase;
import com.example.agrosmart.presentation.viewmodels.HomeViewModel;

public class HomeViewModelFactory implements ViewModelProvider.Factory {
    private final CropsUseCase cropsUseCase;

    public HomeViewModelFactory(CropsUseCase cropsUseCase) {
        this.cropsUseCase = cropsUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(cropsUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
