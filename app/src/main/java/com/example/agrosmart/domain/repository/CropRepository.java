package com.example.agrosmart.domain.repository;

import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.data.local.dto.CropDTO;
import com.example.agrosmart.domain.models.Crop;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface CropRepository {
    CompletableFuture<List<Crop>> getCrops();
    void getCropByName(String name, CropsCallback callback);
}
