package com.example.agrosmart.data.network

import com.example.agrosmart.core.utils.interfaces.CropsCallback
import com.example.agrosmart.domain.models.Crop
import com.example.agrosmart.domain.repository.CropRepository
import java.util.concurrent.CompletableFuture

class CropService(private val repository: CropRepository) {
    val cropsFromFirebase: CompletableFuture<MutableList<Crop?>?>?
        get() = repository.crops

    fun getCropByName(
        name: String?,
        callback: CropsCallback?
    ) {
        repository.getCropByName(
                name,
                callback
        )
    }
}
