package com.example.agrosmart.presentation.viewmodels;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.domain.designModels.CropCarouselData;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.usecase.CropsUseCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<CropCarouselData>> cropsData = new MutableLiveData<>();

    public LiveData<List<CropCarouselData>> getCrops(){
        return cropsData;
    }

    public void loadCrops() {
        CropsUseCase useCase = new CropsUseCase();

        List<Crop> crops = useCase.getCrops(new CropsCallback() {
            @Override
            public void onCropsLoaded(List<Crop> crops) {
                List<CropCarouselData> data = new ArrayList<>();
                if(!crops.isEmpty()){
                    for(Crop c: crops){
                        System.out.println(c.getContent());
                        data.add(createCropInfo(c));
                    }
                    cropsData.setValue(data);
                } else {
                    cropsData.setValue(Collections.emptyList());
                }
            }

            @Override
            public void onError(Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public CropCarouselData createCropInfo(Crop c){
        return new CropCarouselData(getCropImage(c.getCropName()), c.getCropName(), c.getDescription());
    }

    private int getCropImage(String cropName) {
        switch (cropName.toLowerCase(Locale.ROOT)) {
            case "maíz": return R.drawable.imagen_1;
            case "frijol": return R.drawable.sorgo;
            default: return R.drawable.frijol;
        }
    }
}
