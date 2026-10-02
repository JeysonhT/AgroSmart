package com.example.agrosmart.domain.usecase

import android.graphics.Bitmap
import com.example.agrosmart.data.local.dto.MMLResultDTO
import com.example.agrosmart.data.local.ml.DetectionService
import org.tensorflow.lite.support.image.TensorImage
import javax.inject.Inject

class DetectionUseCase @Inject constructor(
    private val service: DetectionService
) {

    fun bitMapToTensor(bitmap: Bitmap?): TensorImage {
        return service.bitmapToTensor(bitmap)
    }

    fun processDetection(
        tensorImage: TensorImage
    ): MMLResultDTO {
        return service.processDetection(
                tensorImage
        )
    }
}
