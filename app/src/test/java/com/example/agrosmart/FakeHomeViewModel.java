package com.example.agrosmart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.core.utils.interfaces.IHomeViewModel;
import com.example.agrosmart.domain.designModels.CropCarouselData;

import java.util.Collections;
import java.util.List;

public class FakeHomeViewModel extends ViewModel implements IHomeViewModel {

    private final MutableLiveData<List<CropCarouselData>> fakeCrops = new MutableLiveData<>();

    public FakeHomeViewModel(){
    }

    @Override
    public LiveData<List<CropCarouselData>> getCrops(){return fakeCrops;}

    @Override
    public void loadCrops() {
        fakeCrops.setValue(Collections.emptyList());
    }
}
