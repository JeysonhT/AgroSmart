package com.example.agrosmart.presentation.ui.components.detection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.agrosmart.core.utils.classes.ImageCacheManager
import com.example.agrosmart.core.utils.classes.ImageEncoder
import com.example.agrosmart.core.utils.classes.NetworkChecker
import com.example.agrosmart.data.local.dto.MMLResultDTO
import com.example.agrosmart.domain.models.DetectionResult
import com.example.agrosmart.domain.models.MMLStats
import com.example.agrosmart.presentation.viewmodels.DetectionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

private const val TAG = "CameraRouteScreen"

@Composable
fun CameraRouteScreen(
    viewModel: DetectionViewModel,
    onDetectionComplete: (result: String, cachedPath: String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var isProcessing by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun compressBitmap(bm: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
        return outputStream.toByteArray()
    }

    fun resizeBitmap(bm: Bitmap): Bitmap {
        return bm.scale(224, 224)
    }

    fun imageToBitmap(image: ImageProxy): Bitmap {
        val buffer: ByteBuffer = image.planes[0].buffer
        val jpegBytes = ByteArray(buffer.remaining())
        buffer.get(jpegBytes)
        val bitmap = BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.size)
        val matrix = Matrix().apply {
            postRotate(image.imageInfo.rotationDegrees.toFloat())
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun processAndNavigate(bitmap: Bitmap, byteArray: ByteArray) {
        scope.launch(Dispatchers.Default) {
            val tensorImage = viewModel.bitmapToTensor(bitmap)
            val resultDTO: MMLResultDTO = viewModel.processDetection(tensorImage, context)

            if (NetworkChecker.isInternetAvailable(context)) {
                val stats = MMLStats().apply {
                    memoryUse = resultDTO.memoryUse
                    inferenceTime = resultDTO.inferenceTime
                    SetInferenceDataFromArray(resultDTO.inferenceData)
                }
                viewModel.sendStatsToFirebase(stats)
                viewModel.sendResulToFirebase(
                    DetectionResult(
                        ImageEncoder.encoderBase64(byteArray),
                        resultDTO.result
                    )
                )
            }

            val resultado = resultDTO.result

            withContext(Dispatchers.Main) {
                isProcessing = false
                if (resultado.isNullOrEmpty()) {
                    errorMessage = "No se encontró ningún resultado en la imagen"
                } else {
                    try {
                        val cachedPath = ImageCacheManager.saveImageToCache(context, byteArray)
                        onDetectionComplete(resultado, cachedPath.orEmpty())
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "Error al guardar imagen en caché"
                    }
                }
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    if (bitmap != null) {
                        isProcessing = true
                        val resized = resizeBitmap(bitmap)
                        val bytes = compressBitmap(resized)
                        processAndNavigate(resized, bytes)
                    }
                }
            } catch (e: Exception) {
                isProcessing = false
                errorMessage = "No se pudo cargar la imagen de la galería"
                Log.e(TAG, "Error cargando imagen", e)
            }
        }
    }

    CameraScreen(
        onPreviewViewReady = { previewView ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.surfaceProvider = previewView.surfaceProvider
                    }

                    val capture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()

                    imageCapture = capture

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        capture
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Error vinculando caso de uso de CameraX", e)
                    errorMessage = "Error al inicializar la cámara: ${e.message}"
                }
            }, ContextCompat.getMainExecutor(context))
        },
        onCaptureClick = {
            val capture = imageCapture
            if (capture == null) {
                errorMessage = "La cámara no está lista"
                return@CameraScreen
            }

            isProcessing = true
            capture.takePicture(
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        super.onCaptureSuccess(image)
                        val bitmap = imageToBitmap(image)
                        image.close()
                        val bytes = compressBitmap(bitmap)
                        processAndNavigate(bitmap, bytes)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        super.onError(exception)
                        isProcessing = false
                        errorMessage = "Error al capturar la foto: ${exception.message}"
                    }
                }
            )
        },
        onSearchImageClick = {
            galleryLauncher.launch("image/*")
        },
        onBackClick = onBackClick,
        modifier = modifier,
        isProcessing = isProcessing,
        errorMessage = errorMessage,
        onDismissError = { errorMessage = null }
    )
}
