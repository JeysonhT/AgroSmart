package com.example.agrosmart.presentation.ui.fragment.subfragment.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.agrosmart.databinding.FragmentPersonalDataBinding;
import com.example.agrosmart.domain.models.DiagnosisHistory;
import com.example.agrosmart.core.utils.classes.PdfGenerator;
import com.example.agrosmart.presentation.viewmodels.PersonalDataViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PersonalDataFragment extends Fragment {

    private FragmentPersonalDataBinding binding;
    private PersonalDataViewModel viewModel;

    private static final int CREATE_CSV_FILE_REQUEST_CODE = 42;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPersonalDataBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(PersonalDataViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<DiagnosisHistory> historyList =  new ArrayList<>();

        viewModel.listOfCrops.observe(getViewLifecycleOwner(), crops -> {
            binding.listOfCropsData.setText(String.join("\n", crops));
        });

        viewModel.listOfDeficiencies.observe(getViewLifecycleOwner(), deficiencies -> {
            binding.listOfDeficienciesData.setText(String.join("\n", deficiencies));
        });

        viewModel.recommendationsGenerated.observe(getViewLifecycleOwner(), recommendationsGenerated -> {
            binding.numberOfRecommendationsGenerated.setText(String.valueOf(recommendationsGenerated));
        });

        viewModel.recommendationsSaved.observe(getViewLifecycleOwner(), recommendationsSaved -> {
            binding.numberOfRecommendationsSaved.setText(String.valueOf(recommendationsSaved));
        });

        // Observar el historial de diagnósticos del ViewModel
        viewModel.diagnosisHistory.observe(getViewLifecycleOwner(), histories -> {
            if (histories != null && !histories.isEmpty()) {
                // Si hay historial y se ha pulsado el botón, generar el PDF
                historyList.clear();
                historyList.addAll(histories);
            }
        });

        viewModel.loadDiagnosisHistory();

        binding.savePDFButton.setOnClickListener(v -> {
            // Cuando se pulsa el botón, se le pide al ViewModel que cargue el historial
            // El observador de diagnosisHistory se encargará de generar el PDF una vez que los datos estén disponibles
            if(!historyList.isEmpty()){
                PdfGenerator.generateDiagnosisHistoryPdf(requireContext(), historyList);
            }
        });

        binding.csvExportButton.setOnClickListener(v -> {
            if (viewModel.diagnosisHistory.getValue() != null && !viewModel.diagnosisHistory.getValue().isEmpty()) {
                createCsvFile();
            } else {
                Toast.makeText(requireContext(), "No hay datos de historial para exportar a CSV.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createCsvFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        intent.putExtra(Intent.EXTRA_TITLE, "AgroSmart_Diagnosticos_" + timeStamp + ".csv");
        startActivityForResult(intent, CREATE_CSV_FILE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_CSV_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                viewModel.exportDiagnosisHistoryToCsv(requireContext(), data.getData());
                Toast.makeText(requireContext(), "Exportando datos a CSV...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Error al crear el archivo CSV.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}