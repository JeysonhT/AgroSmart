package com.example.agrosmart.presentation.ui.fragment.subfragment.home;

import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.fragment.NavHostFragment;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.classes.ImageCacheManager;
import com.example.agrosmart.databinding.FragmentDeficiencyInfoBinding;
import com.example.agrosmart.presentation.ui.adapter.FertilizersAdapter;
import com.example.agrosmart.presentation.viewmodels.FertilizerViewModel;

import java.util.ArrayList;

public class DeficiencyInfoFragment extends Fragment {

    private final String TAG = "DEFICIENCY_INFO_FRAGMENT";
    private FragmentDeficiencyInfoBinding binding;

    private NavController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentDeficiencyInfoBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        controller = NavHostFragment.findNavController(this);
        if(bundle!=null){

            if (ImageCacheManager.loadImageFromCache(requireContext(), bundle.getString("imageuri")) == null) {
                controller.navigateUp();
            }

            try{
                binding.deficiencyImageInfo.setImageBitmap(ImageCacheManager.loadImageFromCache(requireContext(), bundle.getString("imageuri")));
                binding.deficiencyNameInfo.setText(bundle.getString("deficiencyName"));

                binding.deficiencyDescriptionTxt.setText(
                        (bundle.getString("description")!=null) ? bundle.getString("description") : ""
                );

                binding.deficiencySymptomsTxt.setText(
                        (bundle.getString("symptoms")!=null) ? bundle.getString("symptoms") : ""
                );

                binding.deficiencySolutionsTxt.setText(
                        (bundle.getString("solutions")!=null) ? bundle.getString("solutions") : ""
                );
            } catch (NullPointerException e){
                Log.e(TAG, String.format("Error al obtener los valores: %s", e.getMessage()));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ImageCacheManager.cleanupCache(requireContext());
    }
}