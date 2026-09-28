package com.example.agrosmart.data.local.ml

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.agrosmart.core.utils.classes.MemoryMonitor.memorySnapshot
import com.example.agrosmart.data.local.dto.MMLResultDTO
import com.example.agrosmart.ml.ModelAgrosmart
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class DetectionService {
    private val TAG = "DETECTION_SERVICE"

    fun bitmapToTensor(bitmap: Bitmap?): TensorImage? {
        //se carga el bitmap a un tensor image
        val tensorImage = TensorImage(DataType.UINT8)
        // el tipo de dato unit8 corresponde a 0-255 referente a los bits de una imagen e formato rgb
        tensorImage.load(bitmap)

        // se tienen que normalizar los bits de la imagen en valores de entre 0 y 1 en formato float
        // esto debido al que el modelo entrenado recibe entrada de tipo float32
        val imageProcessor =
            ImageProcessor.Builder() // si el bitmap no estuviera redimensionado lo haría aquí
                .add(
                        ResizeOp(
                                224,
                                224,
                                ResizeOp.ResizeMethod.BILINEAR
                        )
                )
                .add(
                        NormalizeOp(
                                0.0f,
                                255.0f
                        )
                ) // esta función transform los valores
                .build()

        // se manda el tensor image
        return imageProcessor.process(tensorImage)
    }

    fun processDetection(
        tensorImage: TensorImage,
        context: Context
    ): MMLResultDTO {
        var resultado: String?
        val resultDTO = MMLResultDTO()
        try {
            //calcular el uso de memoria
            val timeBefore = System.currentTimeMillis()
            val beforeInference = memorySnapshot
            Log.i(
                    TAG,
                    "Memoria antes (Total PSS): ${beforeInference.totalPss} KB"
            )

            // instancia del modelo de tensorflow entrenado
            val model = ModelAgrosmart.newInstance(context)

            // creamos la salida, la cual tendrá como valor el resultado que entregue el modelo
            val outputs = model.process(tensorImage.tensorBuffer)

            //obtenemos todas las clases del archivo de clases
            val clases = readClassFile(context)

            if (!clases.isEmpty()) {
                // obtenemos la salida en un buffer de bytes para proceder a procesarla
                val output = outputs.getOutputFeature0AsTensorBuffer()

                val coincidencias = output.floatArray

                var maxIndex = 0
                var maxProb = 0f
                for (i in coincidencias.indices) {
                    if (coincidencias[i] > maxProb) {
                        maxProb = coincidencias[i]
                        maxIndex = i
                    }
                }
                resultado = clases[maxIndex]

                val afterInference = memorySnapshot
                Log.i(
                        TAG,
                        "Memoria después (Total PSS): " + afterInference.totalPss + " KB"
                )

                val totalPss: Long = afterInference.totalPss - beforeInference.totalPss

                val timeAfter = System.currentTimeMillis()

                val totalTime = timeAfter - timeBefore

                resultDTO.result = resultado
                resultDTO.memoryUse = totalPss
                resultDTO.inferenceTime = totalTime
                resultDTO.inferenceData = coincidencias
            } else {
                throw RuntimeException("Archivo de clases vacio")
            }
        } catch (_: IOException) {
            Log.println(
                    Log.ERROR,
                    TAG,
                    "Model has no loaded"
            )
        }
        return resultDTO
    }

    private fun readClassFile(context: Context): MutableList<String?> {
        val clases: MutableList<String?> = ArrayList<String?>()

        val assetManager = context.assets

        try {
            BufferedReader(
                    InputStreamReader(assetManager.open("labels.txt"))
            ).use { reader ->
                var line: String?
                while ((reader.readLine()
                        .also { line = it }) != null
                ) {
                    clases.add(line)
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        return clases
    }
}
