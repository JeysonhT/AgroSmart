package com.example.agrosmart.data.local.ml;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.agrosmart.core.utils.classes.MemoryMonitor;
import com.example.agrosmart.data.local.dto.MMLResultDTO;
import com.example.agrosmart.ml.AgroSmartMML;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DetectionService {

    private final String TAG = "DETECTION_SERVICE";

    public TensorImage bitmapToTensor(Bitmap bitmap){
        //se carga el bitmap a un tensor image
        TensorImage tensorImage = new TensorImage(DataType.UINT8);
        // el tipo de dato unit8 corresponde a 0-255 referente a los bits de una imagen e formato rgb
        tensorImage.load(bitmap);

        // se tienen que normalizar los bits de la imagen en valores de entre 0 y 1 en formato float
        // esto debido al que el modelo entrenado  recibe entrada de tipo float32
        ImageProcessor imageProcessor = new ImageProcessor.Builder()
        // si el bitmap no estuviera redimencionado lo haria aqui
        .add(new ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
        .add(new NormalizeOp(0.0f, 255.0f)) // esta funcion tranforma los valores
                .build();
        // se manda el tensor image

        return imageProcessor.process(tensorImage);
    }

    public MMLResultDTO processDetection(TensorImage tensorImage, Context context){
        String resultado = "";
        MMLResultDTO resultDTO = new MMLResultDTO();
        try{
            //calcular el uso de memoria
            long timeBefore = System.currentTimeMillis();
            MemoryMonitor.MemorySnapshot beforeInference = MemoryMonitor.getMemorySnapshot();
            Log.i(TAG, "Memoria antes (Total PSS): " + beforeInference.getTotalPss() + " KB");

            // se instancia el modelo de tensorflow entrenado
            AgroSmartMML model = AgroSmartMML.newInstance(context);

            // creamos la salida, la cual tendra como valor el resultado que entregue el modelo
            AgroSmartMML.Outputs outputs = model.process(tensorImage.getTensorBuffer());

                        //obtenemos todas las clases del archivo de clases
                        List<String> clases = readClassFile(context);

                        if(!clases.isEmpty()){
                            // obtenemos la salida en un buffer de bytes para proceder a procesarla
                            TensorBuffer output = outputs.getOutputFeature0AsTensorBuffer();

                            float[] coincidencias = output.getFloatArray();

                            int maxIndex = 0;
                            float maxProb = 0;
                            for(int i = 0; i < coincidencias.length; i++){
                                if(coincidencias[i] > maxProb){
                                    maxProb = coincidencias[i];
                                    maxIndex=i;
                    }
                }
                resultado =  clases.get(maxIndex);

                MemoryMonitor.MemorySnapshot afterInference = MemoryMonitor.getMemorySnapshot();
                Log.i(TAG, "Memoria después (Total PSS): " + afterInference.getTotalPss() + " KB");

                long totalPss = afterInference.getTotalPss() - beforeInference.getTotalPss();

                long timeAfter = System.currentTimeMillis();

                long totalTime = timeAfter - timeBefore;

                resultDTO.setResult(resultado);
                resultDTO.setMemoryUse(totalPss);
                resultDTO.setInferenceTime(totalTime);
                resultDTO.setInferenceData(coincidencias);

            } else {
                throw new RuntimeException("Archivo de clases vacio");
            }
        } catch (IOException e) {
            Log.println(Log.ERROR, TAG, "Model has no loaded");
        }
        return resultDTO;
    }

    private List<String> readClassFile(Context context) {
        List<String> clases = new ArrayList<>();

        AssetManager assetManager = context.getAssets();

        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(assetManager.open("clases.txt")))) {
            String line;

            while((line = reader.readLine()) != null) {
                clases.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return clases;
    }
}
