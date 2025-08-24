package com.example.agrosmart.domain.repository;

import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.domain.models.Crop;

import java.util.List;

public interface CropRepository {
    List<Crop> getCrops(CropsCallback callback);
    void addCropToFavorite(Crop crop);
}
