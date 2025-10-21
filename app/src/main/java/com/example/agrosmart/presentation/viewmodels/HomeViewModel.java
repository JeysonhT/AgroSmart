package com.example.agrosmart.presentation.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.core.utils.interfaces.NewsCallBack;
import com.example.agrosmart.domain.designModels.CropCarouselData;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.models.News;
import com.example.agrosmart.domain.usecase.CropsUseCase;
import com.example.agrosmart.domain.usecase.NewsUseCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HomeViewModel extends ViewModel {
    private final String TAG = "HOME_VIEWMODEL";
    private final MutableLiveData<List<CropCarouselData>> cropsData = new MutableLiveData<>();
    private final MutableLiveData<List<News>> newsData = new MutableLiveData<>();

    public LiveData<List<CropCarouselData>> getCrops(){
        return cropsData;
    }

    public void loadCrops() {
        CropsUseCase useCase = new CropsUseCase();

        useCase.getCrops(new CropsCallback() {
            @Override
            public void onCropsLoaded(List<Crop> crops) {
                List<CropCarouselData> data = new ArrayList<>();
                if(!crops.isEmpty()){
                    for(Crop c: crops){
                        data.add(createCropInfo(c));
                    }
                    cropsData.setValue(data);
                    Log.println(Log.ASSERT, TAG, "Datos cargados exitosamente");
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

    public LiveData<List<News>> getNews(){
        return newsData;
    }

    public void loadNews(){
        NewsUseCase useCase = new NewsUseCase();

        useCase.getNewsUseCase(new NewsCallBack() {
            @Override
            public void onLoaded(List<News> news) {
                newsData.setValue(news);
            }

            @Override
            public void onError(Exception e) {
                Log.println(Log.ERROR, TAG, e.getMessage());
            }
        });
    }

    //metodos auxiliares

    public CropCarouselData createCropInfo(Crop c){
        try{
            return new CropCarouselData(getCropImage(c.getCropName()), c.getType(), c.getDescription());
        } catch(NullPointerException e){
            throw new RuntimeException("Fallo al obtener el nombre del cultivo");
        }

    }

    private int getCropImage(String cropName) {
        switch (cropName.toLowerCase()) {
            case "maiz": return R.drawable.imagen_1;
            case "frijol": return R.drawable.sorgo;
            default: return R.drawable.frijol;
        }
    }
}
