package com.example.agrosmart.core.utils.classes;

import android.util.Base64;
import android.util.Log;

public class ImageEncoder {

    private static final String TAG = "IMAGE_ENCODER";

    public ImageEncoder(){}

    public static String encoderBase64(byte[] image){

        return Base64.encodeToString(image, Base64.DEFAULT);
    }

    public static byte[] decoderBase64(String base64String){
        try {
            if(base64String.contains(",")){
                base64String = base64String.split(",")[1];
            }

            return Base64.decode(base64String, Base64.DEFAULT);

        } catch (Exception e){
            Log.println(Log.ERROR, TAG, "Error to convert image");
            return null;
        }
    }
}
