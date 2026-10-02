package com.example.agrosmart.core.utils.classes

import android.util.Base64
import android.util.Log

object ImageEncoder {

    private const val TAG = "IMAGE_ENCODER"

    fun encoderBase64(image: ByteArray): String {
        return Base64.encodeToString(image, Base64.DEFAULT)
    }

    fun decoderBase64(base64String: String?): ByteArray? {
        if (base64String.isNullOrBlank()) return null
        return try {
            val cleanBase64 = if (base64String.contains(",")) {
                base64String.substringAfter(",")
            } else {
                base64String
            }
            Base64.decode(cleanBase64.trim(), Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e(TAG, "Error to convert image: ${e.message}", e)
            null
        }
    }
}