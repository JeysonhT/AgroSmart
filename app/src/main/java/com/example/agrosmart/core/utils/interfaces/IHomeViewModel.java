package com.example.agrosmart.core.utils.interfaces;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.agrosmart.domain.designModels.CropCarouselData;
import com.example.agrosmart.domain.models.Crop;

import java.util.List;

public interface IHomeViewModel {
    void loadCrops();

    LiveData<List<CropCarouselData>> getCrops();
}
