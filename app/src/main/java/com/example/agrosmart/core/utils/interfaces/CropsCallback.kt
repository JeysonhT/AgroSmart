package com.example.agrosmart.core.utils.interfaces

import com.example.agrosmart.domain.models.Crop

interface CropsCallback {
    fun onCropsLoaded(crops: List<Crop>?)
    fun onError(e: Exception)
}
