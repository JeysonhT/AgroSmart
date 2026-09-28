package com.example.agrosmart.domain.repository

import com.example.agrosmart.core.utils.interfaces.CropsCallback
import com.example.agrosmart.domain.models.Crop
import java.util.concurrent.CompletableFuture

interface CropRepository {
    val crops: CompletableFuture<MutableList<Crop?>?>?
    fun getCropByName(
        name: String?,
        callback: CropsCallback?
    )
}
