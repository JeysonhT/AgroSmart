package com.example.agrosmart.presentation.ui.fragment.subfragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.classes.ImageCacheManager;
import com.example.agrosmart.databinding.FragmentFertilizerInfoBinding;

public class FertilizerInfoFragment extends Fragment {

    private final String TAG = "FERTILIZER_INFO_FRAGMENT";
    private FragmentFertilizerInfoBinding binding;

    private NavController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFertilizerInfoBinding.inflate(inflater, container, false);

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
            try {
                binding.fertilizeImageInfo.setImageBitmap(ImageCacheManager.loadImageFromCache(requireContext(), bundle.getString("imageuri")));

                binding.fertilizerNameInfo.setText(bundle.getString("name"));

                binding.fertilizerDescriptionTxt.setText(
                        (bundle.getString("description")!=null) ? bundle.getString("description") : ""
                );

                binding.fertilizerTypeTxt.setText(
                        (bundle.getString("type")!=null) ? bundle.getString("type") : ""
                );

                binding.fertilizerProviderTxt.setText(
                        (bundle.getString("provider")!=null) ? bundle.getString("provider") : ""
                );

                binding.fertilizerAMethodTxt.setText(
                        (bundle.getString("applicationMethod")!=null) ? bundle.getString("applicationMethod") : ""
                );

                binding.fertilizerRDoseTxt.setText(
                        (bundle.getString("recommendedDose") != null) ? bundle.getString("recommendedDose") : ""
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