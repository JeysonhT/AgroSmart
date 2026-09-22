package com.example.agrosmart.presentation.ui.fragment.subfragment;

import android.content.DialogInterface;
import android.graphics.Bitmap;
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
import androidx.navigation.fragment.NavHostFragment;

import com.example.agrosmart.core.utils.classes.ClickMode;
import com.example.agrosmart.core.utils.classes.ImageCacheManager;
import com.example.agrosmart.core.utils.classes.LoaderDialog;
import com.example.agrosmart.databinding.FragmentDiagnosisInfoBinding;
import com.example.agrosmart.presentation.viewmodels.DetectionFragmentViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DiagnosisInfoFragment extends Fragment {

    private FragmentDiagnosisInfoBinding binding;

    private final String TAG = "DIAGNOSIS_DATE_FRAGMENT";

    private DetectionFragmentViewModel viewModel;

    private LoaderDialog loaderDialog;

    private NavController controller;

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
        viewModel = new ViewModelProvider(this).get(DetectionFragmentViewModel.class);

        loaderDialog = new LoaderDialog(requireContext());

        controller = NavHostFragment.findNavController(this);

        renderUi(bundle);
    }

    private void renderUi(Bundle bundle){
        if(bundle!=null){


            Bitmap image = ImageCacheManager.
                    loadImageFromCache(getContext(), bundle.getString("cropImage"));
            String _id = bundle.getString("idDiagnosis");
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
                    assert recommendation != null;
                    binding.recommendationText.setText(recommendation.replaceAll("[\\t*]+", ""));
                } else {
                    controller.navigateUp();
                }
            } catch (NullPointerException e){
                Log.println(Log.ERROR, TAG, "Error: " + e.getMessage());
            }
            generateRecommendation(_id, diagnosisName);
        }
    }

    private void generateRecommendation(String _id, String diagnosis){
        //testear todo este flujo
        viewModel.getRecommendationResponse().observe(getViewLifecycleOwner(), respuesta -> {
            if(respuesta!=null){

                if(respuesta.getRespuesta().equals("error")){
                    hideLoader();
                    mostrarDialogo("Error", "Conexión muy debil para generar la recomendación\nIntente luego");
                    return;
                }

                hideLoader();
                binding.recommendationText.setText(respuesta.getRespuesta().replaceAll("[\\t*]+", ""));

                viewModel.saveRecommendationInDiagnosis(_id, respuesta.getRespuesta().replaceAll("[\\t*]+", ""));

            }
        });

        binding.btnGenerateRecommendation.setOnClickListener(v -> {
            if(binding.recommendationText.getText().length() < 30){
                viewModel.obtenerRecomendacion(diagnosis);
                showLoader();
            }
        });
    }

    private void showLoader() {
        if (loaderDialog != null && !loaderDialog.isShowing()) {
            loaderDialog.show();
        }
    }

    private void hideLoader() {
        if (loaderDialog != null && loaderDialog.isShowing()) {
            loaderDialog.dismiss();
        }
    }

    private void mostrarDialogo(String title, String message) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    @Override
    public void onDestroyView() {
        ImageCacheManager.cleanupCache(getContext());
        binding = null;
        super.onDestroyView();

    }
}