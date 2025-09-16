package com.example.agrosmart.core.utils.interfaces;

import com.example.agrosmart.data.local.dto.CropDTO;
import com.example.agrosmart.domain.models.Crop;

import java.util.List;

public interface CropsCallback {
    void onCropsLoaded(List<Crop> crops);
    void onError(Exception e);
}
