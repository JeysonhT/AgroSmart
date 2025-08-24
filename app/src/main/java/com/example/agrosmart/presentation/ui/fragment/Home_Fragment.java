package com.example.agrosmart.presentation.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agrosmart.R;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.presentation.ui.adapter.CropInfoAdapter;
import com.example.agrosmart.presentation.ui.adapter.NewsAdapter;
import com.example.agrosmart.domain.designModels.CropCarouselData;
import com.example.agrosmart.domain.models.News;
import com.example.agrosmart.presentation.viewmodels.HomeViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Home_Fragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView noticeView;

    private HomeViewModel viewModel;
    private CropInfoAdapter cropAdapter;
    private NewsAdapter newsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // RecyclerView crops
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        cropAdapter = new CropInfoAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(cropAdapter);

        // Observa los cultivos
        viewModel.getCrops().observe(getViewLifecycleOwner(), cropData -> {
            if (cropData != null && !cropData.isEmpty()) {
                cropAdapter.updateData(cropData);
            }
        });

        viewModel.loadCrops();

        // RecyclerView noticias
        noticeView = view.findViewById(R.id.noticeRecyclerView);
        noticeView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        newsAdapter = new NewsAdapter(getContext(), loadNews());
        noticeView.setAdapter(newsAdapter);
    }

    private List<News> loadNews() {
        List<News> newsList = new ArrayList<>();
        newsList.add(new News(
                R.drawable.frijoles,
                "Avanza siembra de frijol rojo en apante en Nicaragua durante el ciclo 2024/2025",
                "Nicaragua produce más frijoles, pero es casi imposible llevarlos a la mesa por elevados precios",
                "2024-04-04",
                "por aqui va la informacion"
        ));
        return newsList;
    }

}