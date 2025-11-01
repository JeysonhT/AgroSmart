package com.example.agrosmart.presentation.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.classes.ImageCacheManager;
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
    private final MutableLiveData<Boolean> isSavedNew = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isDeletedNew = new MutableLiveData<>();
    private final NewsUseCase useCase = new NewsUseCase();

    public LiveData<List<CropCarouselData>> getCrops(){
        return cropsData;
    }

    public LiveData<Boolean> getSaveNewResult(){return isSavedNew;}

    public LiveData<Boolean> getDeletedNewResult(){return isDeletedNew;}
    public void loadCrops() {
        CropsUseCase useCase = new CropsUseCase();

        List<CropCarouselData> placeholderList= new ArrayList<>();

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
                    placeholderList.add(
                       new CropCarouselData(R.drawable.no_internet_placeholder,
                               "No hay conexión a internet",
                               "",
                               "",
                               ""));
                    cropsData.setValue(placeholderList);
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

    public void loadNews(Context context){
        useCase.getNewsUseCase(context).
                thenAccept(newsData::postValue)
                .exceptionally( e-> {
                    Log.e(TAG, String.format("Error al cargar las noticias: %s", e.getMessage()));
                    return null;
                });
    }

    public void saveNewOnLocal(News news){
        useCase.saveNewOnLocal(news)
                .thenAccept(isSavedNew::postValue).
                exceptionally(e -> {
                    Log.e(TAG, String.format("Error al guardar la noticia: %s", e.getMessage()));
                    return null;
                });
    }

    public void deleteFromLocal(String _id){
        useCase.deleteFromLocal(_id)
                .thenAccept(isDeletedNew::postValue)
                .exceptionally(e->{
                    Log.e(TAG, String.format("Error al borrar la noticia: %s", e.getMessage()));
                    return null;
                });
    }

    //metodos auxiliares

    public CropCarouselData createCropInfo(Crop c){
        try{
            return new CropCarouselData(getCropImage(c.getCropName()), c.getCropName(), c.getDescription(), c.getHarvestTime(), c.getType());
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
