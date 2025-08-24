package com.example.agrosmart.domain.usecase;

import com.example.agrosmart.core.utils.interfaces.CropsCallback;
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

    public List<Crop> getCrops(CropsCallback callback){
        List<Crop> cropsData = new ArrayList<>();
        cropsData = service.getCropsFromFirebase(callback);
        if(cropsData.isEmpty()){
            System.out.println("lista vacia");
        }
        return cropsData;
    }
}
