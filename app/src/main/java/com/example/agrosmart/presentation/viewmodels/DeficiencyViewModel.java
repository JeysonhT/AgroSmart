package com.example.agrosmart.presentation.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.data.network.DeficienciesService;
import com.example.agrosmart.domain.models.Deficiency;
import com.example.agrosmart.domain.usecase.DeficiencyUseCase;

import java.util.List;

public class DeficiencyViewModel extends ViewModel {
    private final String TAG = "DEFICIENCY_VIEW_MODEL";

    private DeficiencyUseCase useCase;

    private final MutableLiveData<List<Deficiency>> deficiencyData = new MutableLiveData<>();

    public LiveData<List<Deficiency>> getData(){return deficiencyData;}

    public void loadData(Context context){
        useCase = new DeficiencyUseCase();

        useCase.getDeficiencies(context)
                .thenAccept(deficiencyData::postValue).exceptionally( e -> {
                    Log.e(TAG, String.format("Error al obtener los datos: %s", e.getMessage()));
                    return null;
                });
    }
}
