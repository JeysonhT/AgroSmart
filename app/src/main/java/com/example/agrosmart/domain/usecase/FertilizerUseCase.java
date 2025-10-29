package com.example.agrosmart.domain.usecase;

import android.content.Context;

import com.example.agrosmart.core.utils.classes.NetworkChecker;
import com.example.agrosmart.data.network.FertilizersService;
import com.example.agrosmart.data.repository.impl.FertilizerRepositoryImpl;
import com.example.agrosmart.domain.models.Fertilizer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FertilizerUseCase {
    private final FertilizersService service;

    public FertilizerUseCase(){
        this.service = new FertilizersService(new FertilizerRepositoryImpl());
    }

    public CompletableFuture<List<Fertilizer>> getFertilizers(Context context){
        if(NetworkChecker.isInternetAvailable(context)){
            return service.getFertilizers();
        }

        return CompletableFuture.completedFuture(Collections.emptyList());
    }
}
