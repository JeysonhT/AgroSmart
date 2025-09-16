package com.example.agrosmart.domain.repository;

import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.data.local.dto.CropDTO;


public interface CropRepository {
    void getCrops(CropsCallback callback);
    void getCropByName(String name, CropsCallback callback);
}
