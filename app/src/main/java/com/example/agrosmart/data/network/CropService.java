package com.example.agrosmart.data.network;

import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.data.local.dto.CropDTO;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.repository.CropRepository;

import java.util.List;

public class CropService {
    private CropRepository repository;

    public CropService(CropRepository repository) {
        this.repository = repository;
    }

    public void getCropsFromFirebase(CropsCallback callback){
        repository.getCrops(callback);
    }

    public void getCropByName(String name, CropsCallback callback){
        repository.getCropByName(name, callback);
    }
}
