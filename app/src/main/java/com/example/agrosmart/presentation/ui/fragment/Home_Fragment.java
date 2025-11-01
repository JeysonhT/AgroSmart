package com.example.agrosmart.presentation.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.classes.ImageCacheManager;
import com.example.agrosmart.databinding.FragmentHomeBinding;
import com.example.agrosmart.domain.models.News;
import com.example.agrosmart.presentation.ui.adapter.CropInfoAdapter;
import com.example.agrosmart.presentation.ui.adapter.NewsAdapter;
import com.example.agrosmart.presentation.viewmodels.HomeViewModel;

import java.util.ArrayList;
import java.util.List;


public class Home_Fragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private NavController navController;

    private NewsAdapter noticeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //controlador de navegación
        navController = Navigation.findNavController(view);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // RecyclerView crops
        CropInfoAdapter cropAdapter = new CropInfoAdapter(getContext(), new ArrayList<>(), navController);
        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false));

        binding.recyclerView.setAdapter(cropAdapter);

        // Observa los cultivos
        viewModel.getCrops().observe(getViewLifecycleOwner(), cropData -> {
            if (cropData != null && !cropData.isEmpty()) {
                cropAdapter.updateData(cropData);
            }
        });

        viewModel.loadCrops();
        viewModel.loadNews(requireContext());

        // RecyclerView noticias
        binding.noticeRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL,
                        false));

        noticeAdapter = new NewsAdapter(getContext(), new ArrayList<>(), navController);
        binding.noticeRecyclerView.setAdapter(noticeAdapter);

        renderNews();
        createNavigation();
    }

    private void renderNews() {
        viewModel.getNews().observe(getViewLifecycleOwner(), news -> {

            if(!news.isEmpty()){
                noticeAdapter.updateData(news);
            } else {
                noticeAdapter.updateData(
                        List.of(
                                new News(
                                        ImageCacheManager.drawableToByteArray(ContextCompat.
                                                getDrawable(requireContext(), R.drawable.no_internet_placeholder)),
                                        "No tienes Conexión, intenta conectarte a internet"
                                )
                        )
                );
            }
        });
    }

    private void createNavigation(){
        binding.buttonDeficiencies.setOnClickListener(v-> {
            try {
                NavDirections action = Home_FragmentDirections.actionHomeFragmentToDeficienciesFragment();
                navController.navigate(action);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        });

        binding.buttonFertilizers.setOnClickListener(v->{
            try {
                NavDirections action = Home_FragmentDirections.actionHomeFragmentToFertilizersFragment();
                navController.navigate(action);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        });
    }

}