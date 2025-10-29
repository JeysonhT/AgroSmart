package com.example.agrosmart.domain.usecase;

import android.content.Context;

import com.example.agrosmart.core.utils.classes.NetworkChecker;
import com.example.agrosmart.data.network.DeficienciesService;
import com.example.agrosmart.data.repository.impl.DeficiencyRepositoryImpl;
import com.example.agrosmart.domain.models.Deficiency;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DeficiencyUseCase {
    private final DeficienciesService service;

    public DeficiencyUseCase(){
        this.service = new DeficienciesService(new DeficiencyRepositoryImpl());
    }

    public CompletableFuture<List<Deficiency>> getDeficiencies(Context context){
        if(NetworkChecker.isInternetAvailable(context)){
            return service.getDeficiencies();
        }

        return CompletableFuture.completedFuture(Collections.emptyList());
    }
}
