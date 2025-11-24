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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.classes.ImageCacheManager;
import com.example.agrosmart.core.utils.interfaces.IHomeViewModel;
import com.example.agrosmart.databinding.FragmentHomeBinding;
import com.example.agrosmart.domain.models.News;
import com.example.agrosmart.domain.usecase.CropsUseCase;
import com.example.agrosmart.domain.usecase.NewsUseCase;
import com.example.agrosmart.presentation.ui.adapter.CropInfoAdapter;
import com.example.agrosmart.presentation.ui.adapter.NewsAdapter;
import com.example.agrosmart.presentation.viewmodels.HomeViewModel;
import com.example.agrosmart.presentation.viewmodels.factory.HomeViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

public class Home_Fragment extends Fragment {
    private FragmentHomeBinding binding;
    @Setter
    private IHomeViewModel viewModel;
    private NavController navController;
    private NewsAdapter noticeAdapter;
    private int state = 0;

    private NavController testNavController;

    private final String TAG = "HOME_FRAGMENT";

    public Home_Fragment() {}
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

        setupNavigationController(view);
        setupViewModel();
        setupUI();
        loadData();
    }

    private void setupNavigationController(View view) {
        if (testNavController != null) {
            // Usar NavController inyectado para testing
            navController = testNavController;
        } else {
            try {
                // Comportamiento normal
                navController = Navigation.findNavController(view);
            } catch (IllegalStateException e) {
                // En testing sin NavController inyectado, usar null
                navController = null;
            }
        }
    }

    private void setupViewModel() {
        // Comportamiento normal
        if(viewModel == null){
            CropsUseCase cropsUseCase = new CropsUseCase();
            NewsUseCase newsUseCase = new NewsUseCase();
            HomeViewModelFactory factory = new HomeViewModelFactory(newsUseCase, cropsUseCase);
            viewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
        }
    }

    private void setupUI() {
        setupCropsRecyclerView();
        setupNewsRecyclerView();
        setupNavigationListeners();
    }

    private void setupCropsRecyclerView() {
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
    }

    private void setupNewsRecyclerView() {
        binding.noticeRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL,
                        false));

        noticeAdapter = new NewsAdapter(getContext(), new ArrayList<>(), navController);
        binding.noticeRecyclerView.setAdapter(noticeAdapter);

        setupNewsObserver();
    }

    private void setupNewsObserver() {
        viewModel.getNews().observe(getViewLifecycleOwner(), news -> {
            if (news != null && !news.isEmpty()) {
                noticeAdapter.updateData(news);
            } else {
                showNoConnectionPlaceholder();
            }
        });
    }

    private void showNoConnectionPlaceholder() {
        try {
            News placeholderNews = new News(
                    ImageCacheManager.drawableToByteArray(ContextCompat.getDrawable(requireContext(), R.drawable.no_internet_placeholder)),
                    "No tienes Conexión, intenta conectarte a internet"
            );
            noticeAdapter.updateData(List.of(placeholderNews));
        } catch (Exception e) {
            // En testing, puede fallar, usar datos simples
            News simplePlaceholder = new News(null, "No hay conexión a internet");
            noticeAdapter.updateData(List.of(simplePlaceholder));
        }
    }

    private void loadData() {
        viewModel.loadCrops();
        viewModel.loadNews(requireContext());
    }

    private void setupNavigationListeners() {
        binding.buttonDeficiencies.setOnClickListener(v -> {
            navigateSafely(Home_FragmentDirections.actionHomeFragmentToDeficienciesFragment());
        });

        binding.buttonFertilizers.setOnClickListener(v -> {
            navigateSafely(Home_FragmentDirections.actionHomeFragmentToFertilizersFragment());
        });

        binding.localNews.setOnClickListener(v -> {
            toggleLocalNews();
        });
    }

    private void navigateSafely(NavDirections action) {
        if (navController != null) {
            try {
                navController.navigate(action);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private void toggleLocalNews() {
        try {
            if (state == 0) {
                state = 1;
                viewModel.loadLocalNews();
                binding.localNews.setText("ver noticias");
                binding.localNews.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.apple_green));
            } else {
                state = 0;
                viewModel.loadNews(requireContext());
                binding.localNews.setText(R.string.localNoticeButtonText);
                binding.localNews.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mid_orange));
            }
        } catch (RuntimeException e) {
            Log.e(TAG, e.getMessage());
        }
    }

}