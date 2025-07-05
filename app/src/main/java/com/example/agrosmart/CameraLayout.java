package com.example.agrosmart;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.agrosmart.services.mlservices.DetectionService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.util.concurrent.ListenableFuture;

import org.tensorflow.lite.support.image.TensorImage;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class CameraLayout extends Fragment {
    private static final String TAG = "CameraLayout";

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Button takePhoto;
    private Button searchPhoto;
    private PreviewView previewView;
    private ImageCapture imageCapture;

    private DetectionService detectionService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getContext() != null) {
            cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        } else {
            Log.e(TAG, "el contexto es nulo en la creación");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_layout, container, false);

        takePhoto = view.findViewById(R.id.image_capture_button);
        searchPhoto = view.findViewById(R.id.search_image_button);
        previewView = view.findViewById(R.id.photo_preview);

        setUpCamera();

        takePhoto.setOnClickListener(v -> {
            takeImage();
        });

        return view;
    }

    private void setUpCamera() {
        if (cameraProviderFuture == null || getContext() == null) {
            Log.e(TAG, "Camera provider or context is null");
            return;
        }

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        imageCapture = new ImageCapture.Builder()
                //.setTargetAspectRatio(RATIO_4_3)
                .setTargetResolution(new Size(224, 224))
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        Preview preview = new Preview.Builder().build();

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                getViewLifecycleOwner();
                if (previewView == null) {
                    Log.e(TAG, "LifecycleOwner or previewView is null");
                    return;
                }

                cameraProvider.unbindAll(); // Asegura que no haya otro uso de la cámara
                Camera camera = cameraProvider.bindToLifecycle(
                        getViewLifecycleOwner(),
                        cameraSelector,
                        imageCapture,
                        preview
                );

                previewView.getSurfaceProvider();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error al obtener cameraProvider", e);
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    public void takeImage(){
        Handler handler = new Handler(Looper.getMainLooper());
        imageCapture.takePicture(Executors.newSingleThreadExecutor(), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                super.onCaptureSuccess(image);
                // se usara este bitmap para pasarlo al modelo de machine learning
                Bitmap bitmap = imageToBitMap(image);

                detectionService = new DetectionService();

                // se crea un tensor image a partir de un bitmap sin dimencionar
                TensorImage tensorImage = detectionService.bitmatToTensor(bitmap);

                // se obtiene el resultado de el proceso de detección
                final String resultado = detectionService.processDetection(tensorImage, getContext());

                image.close();

                if(Objects.equals(resultado, "")){
                    handler.post(() -> {
                        mostrarDialogo("No se encontro resultado");
                    });
                } else {
                    handler.post(() -> {
                        mostrarDialogo(resultado);
                    });
                }
            }
        });
    }

    private Bitmap imageToBitMap(ImageProxy imageProxy){
        // se obtiene cada uno de los bytes de la imagen, se toma la imagen jpeg qu se encuentra en el primer plano del image proxy
        ImageProxy.PlaneProxy planeProxy = imageProxy.getPlanes()[0];

        ByteBuffer buffer = planeProxy.getBuffer();

        // se pasa el buffer a un nuevo array de bytes
        byte[] bytes = new byte[buffer.remaining()];

        buffer.get(bytes);

        // decodificación del arreglo de bytes
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        // se usa una matriz de graficos para corregir la rotación de la imagen
        Matrix matrix = new Matrix();
        matrix.postRotate(imageProxy.getImageInfo().getRotationDegrees());

        Bitmap rotatedMap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        // por ultimo se redimenciona el bitmap a 224 * 224
        return Bitmap.createScaledBitmap(rotatedMap,
                224, 224,
                true);
    }

    private void mostrarDialogo(String mensaje) {
                new MaterialAlertDialogBuilder(requireContext())
                 .setTitle("Resultado de diagnostico")
                        .setMessage(mensaje)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                    // me falta  la accion de registrar en el historial
                    }
                }).show();
    }
}
