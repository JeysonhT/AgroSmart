package com.example.agrosmart.data.local.ml;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import com.example.agrosmart.ml.AgroSmartMMLite;

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

    public TensorImage bitmatToTensor(Bitmap bitmap){
        //se carga el bitmap a un tensor image
        TensorImage tensorImage = new TensorImage(DataType.UINT8);
        // el tipo de dato unit9 corresponde a 0-255 referente a los bits de una imagen e formato rgb
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

    public String processDetection(TensorImage tensorImage, Context context){
        String resultado = "";
        try{
            // se instancia el modelo de tensorflow entrenado
            AgroSmartMMLite model = AgroSmartMMLite.newInstance(context);

            // creamos la salida, la cual tendra como valor el resultado que entregue el modelo
            AgroSmartMMLite.Outputs outputs = model.process(tensorImage.getTensorBuffer());

            //obtenemos todas las clases del archivo de clases
            List<String> clases = readClassFile(context, "clases.txt");

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
                    System.out.println("coincidencia: " + i + "= " + coincidencias[i]);
                }
                System.out.println(maxIndex);
                resultado =  clases.get(maxIndex);
            } else {
                throw new RuntimeException("Archivo de clases vacio");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return resultado;
    }

    private List<String> readClassFile(Context context, String uri) {
        List<String> clases = new ArrayList<>();

        AssetManager assetManager = context.getAssets();

        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(assetManager.open(uri)))) {
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
