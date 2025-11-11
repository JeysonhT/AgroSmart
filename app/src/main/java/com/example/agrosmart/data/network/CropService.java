package com.example.agrosmart.data.network;

import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.data.local.dto.CropDTO;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.repository.CropRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CropService {
    private CropRepository repository;

    public CropService(CropRepository repository) {
        this.repository = repository;
    }

    public CompletableFuture<List<Crop>> getCropsFromFirebase(){
        return repository.getCrops();
    }

    public void getCropByName(String name, CropsCallback callback){
        repository.getCropByName(name, callback);
    }
}
