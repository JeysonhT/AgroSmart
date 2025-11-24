package com.example.agrosmart;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import com.example.agrosmart.presentation.ui.fragment.Home_Fragment;

public class HomeFragmentFactory extends FragmentFactory {
    private final FakeHomeViewModel mockViewModel;

    public HomeFragmentFactory(FakeHomeViewModel mockViewModel) {
        this.mockViewModel = mockViewModel;
    }

    @NonNull
    @Override
    public Fragment instantiate(
            @NonNull ClassLoader classLoader,
            @NonNull String className
    ) {
        if (className.equals(Home_Fragment.class.getName())) {
            Home_Fragment fragment = new Home_Fragment();
            fragment.setViewModel(mockViewModel);
            return fragment;
        }
        return super.instantiate(classLoader, className);
    }
}
