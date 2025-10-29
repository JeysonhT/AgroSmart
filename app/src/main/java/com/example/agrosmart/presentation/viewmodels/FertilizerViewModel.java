package com.example.agrosmart.presentation.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.domain.models.Deficiency;
import com.example.agrosmart.domain.models.Fertilizer;
import com.example.agrosmart.domain.usecase.FertilizerUseCase;

import java.util.List;

public class FertilizerViewModel extends ViewModel {

    private final String TAG = "FERTILIZER_VIEW_MODEL";

    private FertilizerUseCase useCase;

    private final MutableLiveData<List<Fertilizer>> fertilizerData = new MutableLiveData<>();

    public LiveData<List<Fertilizer>> getData(){return fertilizerData;}

    public void loadData(Context context){
        useCase = new FertilizerUseCase();

        useCase.getFertilizers(context)
                .thenAccept(fertilizerData::postValue)
                .exceptionally(e-> {
                    Log.e(TAG, String.format("Error al obtener los datos: %s", e.getMessage()));
                    return null;
                });
    }

}
