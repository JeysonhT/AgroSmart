package com.example.agrosmart.presentation.viewmodels;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.domain.models.DiagnosisHistory;
import com.example.agrosmart.domain.usecase.DiagnosisHistoryUseCase;
import com.opencsv.CSVWriter; // New import

import java.io.IOException; // New import
import java.io.OutputStream; // New import
import java.io.OutputStreamWriter; // New import
import java.util.ArrayList; // New import
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PersonalDataViewModel extends ViewModel {

    private final String TAG = "PERSONAL_DATA_VIEW_MODEL";

    private final MutableLiveData<List<String>> _listOfCrops = new MutableLiveData<>();
    public LiveData<List<String>> listOfCrops = _listOfCrops;

    private final MutableLiveData<List<String>> _listOfDeficiencies = new MutableLiveData<>();
    public LiveData<List<String>> listOfDeficiencies = _listOfDeficiencies;

    private final MutableLiveData<Integer> _diagnosisGenerated = new MutableLiveData<>();
    public LiveData<Integer> diagnosisGenerated = _diagnosisGenerated;

    private final MutableLiveData<Integer> _recommendationsSaved = new MutableLiveData<>();
    public LiveData<Integer> recommendationsSaved = _recommendationsSaved;

    private final MutableLiveData<List<DiagnosisHistory>> _diagnosisHistory = new MutableLiveData<>();
    public LiveData<List<DiagnosisHistory>> diagnosisHistory = _diagnosisHistory;

    private final DiagnosisHistoryUseCase diagnosisHistoryUseCase;

    public PersonalDataViewModel() {
        diagnosisHistoryUseCase = new DiagnosisHistoryUseCase();
        loadDiagnosisHistory(); // Cargar el historial al inicializar el ViewModel
    }

    public void loadDiagnosisHistory() {
        diagnosisHistoryUseCase.getHistories().thenAccept(histories -> {
            _diagnosisHistory.postValue(histories);

            int savedCount = 0;
            int diagnosisCount = 0;
            Map<String, Integer> cropCounts = new HashMap<>();
            Map<String, Integer> deficiencyCounts = new HashMap<>();

            if (histories != null) {
                for (DiagnosisHistory history : histories) {
                    try{
                        if(history != null && !history.getDeficiency().isEmpty()){
                            diagnosisCount++;
                        }

                        // Contar recomendaciones guardadas
                        if (history.getRecommendation() != null && !history.getRecommendation().trim().isEmpty()) {
                            savedCount++;
                        }

                        // Contar cultivos
                        if (history.getCrop().getCropName() != null && !history.getCrop().getCropName().trim().isEmpty()) {
                            cropCounts.put(history.getCrop().getCropName(), cropCounts.getOrDefault(history.getCrop().getCropName(), 0) + 1);
                        }

                        // Contar deficiencias
                        if (history.getDeficiency() != null && !history.getDeficiency().trim().isEmpty()) {
                            deficiencyCounts.put(history.getDeficiency(), deficiencyCounts.getOrDefault(history.getDeficiency(), 0) + 1);
                        }

                    } catch (NullPointerException e){
                        Log.e(TAG, String.format("Error al obtener numero de deficincias: ", e.getMessage()));
                    }
                }
            }
            _recommendationsSaved.postValue(savedCount);
            _diagnosisGenerated.postValue(diagnosisCount);

            // Ordenar y obtener los cultivos más frecuentes (top 3)
            List<String> topCrops = cropCounts.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            _listOfCrops.postValue(topCrops);

            // Ordenar y obtener las deficiencias más frecuentes (top 3)
            List<String> topDeficiencies = deficiencyCounts.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            _listOfDeficiencies.postValue(topDeficiencies);

        });
    }

    public void exportDiagnosisHistoryToCsv(Context context, Uri uri) {
        List<DiagnosisHistory> histories = _diagnosisHistory.getValue();
        if (histories == null || histories.isEmpty()) {
            Log.w(TAG, "No diagnosis history available to export.");
            return;
        }

        try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
             OutputStreamWriter writer = new OutputStreamWriter(outputStream);
             CSVWriter csvWriter = new CSVWriter(writer)) {

            String[] header = {"Fecha", "Cultivo", "Deficiencia", "Recomendacion"};
            csvWriter.writeNext(header);

            for (DiagnosisHistory history : histories) {
                List<String> row = new ArrayList<>();
                row.add(history.getDiagnosisDate() != null ? history.getDiagnosisDate().toString() : "");
                row.add(history.getCrop() != null && history.getCrop().getCropName() != null ? history.getCrop().getCropName() : "");
                row.add(history.getDeficiency() != null ? history.getDeficiency() : "");
                row.add(history.getRecommendation() != null ? history.getRecommendation() : "");
                csvWriter.writeNext(row.toArray(new String[0]));
            }

            Log.i(TAG, "CSV file exported successfully to " + uri.toString());

        } catch (IOException e) {
            Log.e(TAG, "Error exporting CSV file", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "Null pointer exception during CSV export, check data integrity.", e);
        }
    }
}
