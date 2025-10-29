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
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.agrosmart.R;
import com.example.agrosmart.databinding.FragmentDeficienciesBinding;
import com.example.agrosmart.domain.models.Deficiency;
import com.example.agrosmart.presentation.ui.adapter.DeficienciesAdapter;
import com.example.agrosmart.presentation.viewmodels.DeficiencyViewModel;

import java.util.ArrayList;
import java.util.List;

public class DeficienciesFragment extends Fragment {

    private FragmentDeficienciesBinding binding;

    private DeficiencyViewModel viewModel;

    private DeficienciesAdapter adapter;

    private NavController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDeficienciesBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        controller = Navigation.findNavController(view);

        viewModel = new ViewModelProvider(this).get(DeficiencyViewModel.class);

        viewModel.loadData(requireContext());

        adapter = new DeficienciesAdapter(Navigation.findNavController(view), new ArrayList<>());

        binding.deficienciesRecyclerView.setLayoutManager(new LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
        ));

        binding.deficienciesRecyclerView.setAdapter(adapter);

        loadData();
        createNavigation();
    }

    private void loadData(){

        viewModel.getData().observe(getViewLifecycleOwner(), deficiencies -> {
            if(deficiencies != null && !deficiencies.isEmpty()){
                adapter.updateData(deficiencies);
            } else {
                adapter.updateData(List.of(
                        new Deficiency("No hay datos")
                ));
            }
        });

    }

    private void createNavigation(){
        binding.backButtonDeficiencies.setOnClickListener(v->{
            controller.navigateUp();
        });
    }
}