package com.example.agrosmart.presentation.ui.fragment.subfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.agrosmart.R;
import com.example.agrosmart.databinding.FragmentCropInfoBinding;

import java.util.Objects;

public class CropInfoFragment extends Fragment {

    private final String TAG = "CROP_INFO_FRAGMENT";

    private FragmentCropInfoBinding binding;

    private NavController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCropInfoBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        controller = NavHostFragment.findNavController(this);

        Bundle bundle = getArguments();

        render(bundle);
    }

    private void render(Bundle bundle){

        if(bundle!=null){
            try{

                binding.cropInfoImage.setImageResource(bundle.getInt("image"));
                binding.cropName.setText(bundle.getString("title"));
                binding.cropDescription.setText(bundle.getString("description"));
                binding.cropHarvestTime.setText(bundle.getString("harvestTime"));
                binding.cropType.setText(bundle.getString("type"));

                // back button
                binding.backButtonCropInfo.setOnClickListener( v -> {
                    controller.navigateUp();
                });

            } catch (NullPointerException e){
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            }
        }
    }
}