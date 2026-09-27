package com.example.agrosmart.core.utils.interfaces

import androidx.lifecycle.LiveData
import com.example.agrosmart.domain.designModels.CropCarouselData

interface IHomeViewModel {
    fun loadCrops()
    val crops: LiveData<List<CropCarouselData>>
}
