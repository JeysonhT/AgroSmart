package com.example.agrosmart.data.network;

import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.repository.CropRepository;

import java.util.ArrayList;
import java.util.List;

public class CropService {
    private CropRepository repository;

    public CropService(CropRepository repository) {
        this.repository = repository;
    }

    public List<Crop> getCropsFromFirebase(CropsCallback callback){
        List<Crop> cropData = new ArrayList<>();
        cropData = repository.getCrops(callback);
        if(!cropData.isEmpty()){
            System.out.println(cropData.get(0).getCropName());
        }
        return cropData;
    }
}
