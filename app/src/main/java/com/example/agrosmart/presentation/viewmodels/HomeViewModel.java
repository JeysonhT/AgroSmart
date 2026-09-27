package com.example.agrosmart.presentation.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.interfaces.IHomeViewModel;
import com.example.agrosmart.domain.designModels.CropCarouselData;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.usecase.CropsUseCase;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel implements IHomeViewModel {
    private final String TAG = "HOME_VIEWMODEL";
    private final MutableLiveData<List<CropCarouselData>> cropsData = new MutableLiveData<>();
    private final CropsUseCase cropsUseCase;

    public HomeViewModel(CropsUseCase cropsUseCase) {
        this.cropsUseCase = cropsUseCase;
    }

    @Override
    public LiveData<List<CropCarouselData>> getCrops(){
        return cropsData;
    }

    @Override
    public void loadCrops() {
        List<CropCarouselData> placeholderList= new ArrayList<>();

        cropsUseCase.getCrops()
                        .thenAccept(crops -> {
                            List<CropCarouselData> data = new ArrayList<>();
                            if(!crops.isEmpty()){
                                for(Crop c: crops){
                                    data.add(createCropInfo(c));
                                }

                                cropsData.postValue(data);
                                Log.println(Log.ASSERT, TAG, "Datos cargados exitosamente");
                            } else {
                                placeholderList.add(
                                        new CropCarouselData(R.drawable.no_internet_placeholder,
                                                "No hay conexión a internet",
                                                "",
                                                "",
                                                ""));
                                cropsData.postValue(placeholderList);
                            }
                        })
                .exceptionally(e -> {
                    Log.e(TAG, String.format("Error al cargar los cultivos: %s", e.getMessage()));
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
