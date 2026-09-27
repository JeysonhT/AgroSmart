package com.example.agrosmart.domain.usecase;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.agrosmart.data.local.dto.MMLResultDTO;
import com.example.agrosmart.data.local.ml.DetectionService;

import org.tensorflow.lite.support.image.TensorImage;
import javax.inject.Inject;

public class DetectionUseCase {
    private final DetectionService service;

    @Inject
    public DetectionUseCase(){
        this.service = new DetectionService();
    }

    public TensorImage bitMapToTensor(Bitmap bitmap){
        return service.bitmapToTensor(bitmap);
    }

    public MMLResultDTO processDetection(TensorImage tensorImage, Context context){
        return service.processDetection(tensorImage, context);
    }

}
