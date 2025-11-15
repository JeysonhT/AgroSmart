package com.example.agrosmart.core.utils.interfaces;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.agrosmart.domain.designModels.CropCarouselData;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.models.News;

import java.util.List;

public interface IHomeViewModel {
    void loadCrops();
    void loadNews(Context context);
    void loadLocalNews();

    LiveData<List<CropCarouselData>> getCrops();

    LiveData<List<News>> getNews();
}
