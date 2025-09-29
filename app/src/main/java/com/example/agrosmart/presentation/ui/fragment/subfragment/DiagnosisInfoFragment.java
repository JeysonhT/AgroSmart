package com.example.agrosmart.presentation.ui.fragment.subfragment;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.classes.ImageCacheManager;
import com.example.agrosmart.databinding.FragmentDiagnosisInfoBinding;

public class DiagnosisInfoFragment extends Fragment {

    private FragmentDiagnosisInfoBinding binding;

    private final String TAG = "DIAGNOSIS_DATE_FRAGMENT";

    public DiagnosisInfoFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentDiagnosisInfoBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();

        if(bundle!=null){
            Bitmap image = ImageCacheManager.
                    loadImageFromCache(getContext(), bundle.getString("cropImage"));
            String cropName = bundle.getString("cropName");
            String diagnosisDate = bundle.getString("diagnosisDate");
            String diagnosisName = bundle.getString("diagnosisName");
            String recommendation = bundle.getString("recommendation");

            try {

                if(image!=null){
                    binding.cropImageInformation.setImageBitmap(
                            image
                    );
                    binding.cropNameInformation.setText(cropName);
                    binding.dateDiagnosisInformation.setText(diagnosisDate);
                    binding.defiencyText.setText(diagnosisName);
                    binding.recommendationText.setText(recommendation);
                }
            } catch (NullPointerException e){
                Log.println(Log.ERROR, TAG, "Error: " + e.getMessage());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ImageCacheManager.cleanupCache(getContext());
        binding = null;
    }
}