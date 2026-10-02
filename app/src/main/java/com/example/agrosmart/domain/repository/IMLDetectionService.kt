package com.example.agrosmart.domain.repository

import android.graphics.Bitmap
import org.tensorflow.lite.support.image.TensorImage

interface IMLDetectionService {
    fun bitmapToTensor(bitmap: Bitmap): TensorImage?
    suspend fun processDetection(tensorImage: TensorImage)
}