package com.example.agrosmart.presentation.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agrosmart.R;
import com.example.agrosmart.presentation.ui.adapter.CropInfoAdapter;
import com.example.agrosmart.presentation.ui.adapter.NewsAdapter;
import com.example.agrosmart.presentation.viewmodels.HomeViewModel;

import java.util.ArrayList;


public class Home_Fragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView noticeView;

    private HomeViewModel viewModel;
    private CropInfoAdapter cropAdapter;

    private NavController navController;

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
        //controlador de navegación
        navController = Navigation.findNavController(view);

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
        viewModel.loadNews();

        // RecyclerView noticias
        noticeView = view.findViewById(R.id.noticeRecyclerView);
        noticeView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        renderNews();
    }

    private void renderNews() {
        viewModel.getNews().observe(getViewLifecycleOwner(), news -> {
            NewsAdapter adapter = new NewsAdapter(getContext(), new ArrayList<>(), navController);
            noticeView.setAdapter(adapter);
            if(!news.isEmpty()){
                adapter.updateData(news);
            }
        });
    }

}