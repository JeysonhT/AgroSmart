package com.example.agrosmart.domain.usecase;

import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.data.local.dto.CropDTO;
import com.example.agrosmart.data.network.CropService;
import com.example.agrosmart.data.repository.impl.CropRepositoryImpl;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.repository.CropRepository;

import java.util.ArrayList;
import java.util.List;

public class CropsUseCase {
    private CropService service;

    public CropsUseCase() {
        service = new CropService(new CropRepositoryImpl());
    }

    //el servicio retornara los datos por el callback
    public void getCrops(CropsCallback callback){
        service.getCropsFromFirebase(callback);
    }

    public void getCropByName(String name, CropsCallback callback){
        service.getCropByName(name, callback);
    }
}
