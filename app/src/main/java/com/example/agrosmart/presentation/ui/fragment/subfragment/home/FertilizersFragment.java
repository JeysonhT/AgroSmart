package com.example.agrosmart.presentation.ui.fragment.subfragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.agrosmart.R;
import com.example.agrosmart.databinding.FragmentFertilizersBinding;
import com.example.agrosmart.domain.models.Fertilizer;
import com.example.agrosmart.presentation.ui.adapter.FertilizersAdapter;
import com.example.agrosmart.presentation.viewmodels.FertilizerViewModel;

import java.util.ArrayList;
import java.util.List;

public class FertilizersFragment extends Fragment {

    private FragmentFertilizersBinding binding;
    private FertilizerViewModel viewModel;
    private FertilizersAdapter adapter;

    private NavController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFertilizersBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        controller = Navigation.findNavController(view);

        viewModel = new ViewModelProvider(this).get(FertilizerViewModel.class);

        adapter = new FertilizersAdapter(Navigation.findNavController(view), new ArrayList<>());

        binding.fertilizersRecyclerView.setLayoutManager(
                new LinearLayoutManager(
                        getContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                )
        );

        binding.fertilizersRecyclerView.setAdapter(adapter);

        viewModel.loadData(requireContext());

        loadData();
        createNavigation();
    }

    private void loadData(){
        viewModel.getData().observe(getViewLifecycleOwner(), fertilizers -> {
            if(fertilizers!=null && !fertilizers.isEmpty()){
                adapter.updateData(fertilizers);
            } else {
                adapter.updateData(List.of(
                        new Fertilizer("No hay datos")
                ));
            }
        });
    }

    private void createNavigation(){
        binding.backButtonFertilizer.setOnClickListener(v->{
            controller.navigateUp();
        });
    }
}