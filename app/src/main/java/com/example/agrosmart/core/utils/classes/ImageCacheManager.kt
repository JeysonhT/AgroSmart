package com.example.agrosmart.core.utils.classes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

object ImageCacheManager {
    private const val TAG = "IMAGE_CACHE_UTILS"
    private const val TEMP_IMAGE_PREFIX = "temp_image_"

    @Throws(IOException::class)
    fun saveImageToCache(
        context: Context,
        imageBytes: ByteArray?
    ): String? {
        if (!isValidImage(imageBytes)) {
            Log.w(
                    TAG,
                    "Intento de guardar array de bytes vacío o de mal formato"
            )
            return null
        }

        try {
            val filename = TEMP_IMAGE_PREFIX + System.currentTimeMillis() + ".jpeg"
            val file = File(
                    context.getCacheDir(),
                    filename
            )

            FileOutputStream(file).use { fos ->
                fos.write(imageBytes)
                fos.flush()
            }
            return file.absolutePath
        } catch (e: IOException) {
            Log.w(
                    TAG,
                    "Error: " + e.message
            )
            return null
        }
    }

    // validador de formato de imagen
    private fun isValidImage(imageBytes: ByteArray?): Boolean {
        if (imageBytes == null || imageBytes.size < 4) {
            println("Imagen no valida")
            return false
        }

        // Verificar signatures de formatos comunes
        // JPEG: FF D8 FF
        if (imageBytes[0] == 0xFF.toByte() && imageBytes[1] == 0xD8.toByte() && imageBytes[2] == 0xFF.toByte()) {
            println("Imagen de tipo JPEG")
            return true
        }

        // PNG: 89 50 4E 47
        if (imageBytes[0] == 0x89.toByte() && imageBytes[1] == 0x50.toByte() && imageBytes[2] == 0x4E.toByte() && imageBytes[3] == 0x47.toByte()) {
            return true
        }

        // WEBP: RIFF .... WEBP
        if (imageBytes[0] == 'R'.code.toByte() && imageBytes[1] == 'I'.code.toByte() && imageBytes[2] == 'F'.code.toByte() && imageBytes[3] == 'F'.code.toByte()) {
            return true
        }

        return false
    }

    // Cargar imagen desde cache como Bitmap
    fun loadImageFromCache(
        filePath: String?
    ): Bitmap? {
        if (filePath.isNullOrEmpty()) {
            return null
        }

        try {
            val file = File(filePath)
            if (file.exists()) {
                return BitmapFactory.decodeFile(filePath)
            } else {
                Log.w(
                        TAG,
                        "Archivo no encontrado: $filePath"
                )
                return null
            }
        } catch (e: Exception) {
            Log.e(
                    TAG,
                    "Error al cargar imagen desde cache: " + e.message
            )
            return null
        }
    }

    fun getArrayFromFile(
        path: String?
    ): ByteArray? {
        if (path.isNullOrEmpty()) {
            return null
        }

        val file = File(path)
        var loadedByteArray: ByteArray?

        try {
            FileInputStream(file).use { fis ->
                val fileSize = fis.available() // Obtiene el tamaño del archivo
                loadedByteArray = ByteArray(fileSize)
                fis.read(loadedByteArray)
                return loadedByteArray
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    // Limpiar archivos temporales del cache
    fun cleanupCache(context: Context) {
        try {
            val cacheDir = context.cacheDir
            val files = cacheDir.listFiles()

            if (files != null) {
                for (file in files) {
                    if (file.name
                            .startsWith(TEMP_IMAGE_PREFIX)
                    ) {
                        val deleted = file.delete()
                        if (deleted) {
                            Log.d(
                                    TAG,
                                    "Archivo eliminado: " + file.name
                            )
                        } else {
                            Log.w(
                                    TAG,
                                    "No se pudo eliminar: " + file.name
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(
                    TAG,
                    "Error al limpiar cache: " + e.message
            )
        }
    }
}

