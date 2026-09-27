package com.example.agrosmart.core.utils.classes

import android.util.Base64
import android.util.Log


object ImageEncoder {

    private const val TAG = "IMAGE_ENCODER"


    fun encoderBase64(image: ByteArray): String {
        return Base64.encodeToString(image,
                Base64.DEFAULT)
    }

    fun decoderBase64(base64String: String): ByteArray? {
        try {
            var base64 = ""
            if(base64String.contains(",")){
                base64 = base64String.split(",")[1]
            }

            return Base64.decode(base64,
                    Base64.DEFAULT)
        } catch (e: Exception){
            Log.println(
                    Log.ERROR,
                    TAG,
                    "Error to convert image"
            )
            return null
        }
    }
}