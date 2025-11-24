package com.example.agrosmart;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.core.utils.interfaces.IHomeViewModel;
import com.example.agrosmart.domain.designModels.CropCarouselData;
import com.example.agrosmart.domain.models.News;

import java.util.Collections;
import java.util.List;

public class FakeHomeViewModel extends ViewModel implements IHomeViewModel {

    private final MutableLiveData<List<News>> fakeNews = new MutableLiveData<>();
    private final MutableLiveData<List<CropCarouselData>> fakeCrops = new MutableLiveData<>();

    public FakeHomeViewModel(){
    }

    @Override
    public LiveData<List<CropCarouselData>> getCrops(){return fakeCrops;}
    
    public LiveData<List<News>> getNews() {
        return fakeNews;
    }


    @Override
    public void loadCrops() {
        fakeCrops.setValue(Collections.emptyList());
    }

    @Override
    public void loadNews(Context context) {
        fakeNews.setValue(Collections.emptyList());
    }

    @Override
    public void loadLocalNews() {
        fakeNews.setValue(Collections.emptyList());
    }
}
