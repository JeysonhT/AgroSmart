package com.example.agrosmart.presentation.ui.fragment;

import static android.view.Surface.ROTATION_90;

import static androidx.core.content.ContextCompat.getDisplayOrDefault;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.classes.ImageCacheManager;
import com.example.agrosmart.data.local.ml.DetectionService;
import com.example.agrosmart.databinding.FragmentCameraLayoutBinding;
import com.example.agrosmart.presentation.viewmodels.DetectionFragmentViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.util.concurrent.ListenableFuture;

import org.tensorflow.lite.support.image.TensorImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


public class CameraLayout extends Fragment {
    private static final String TAG = "CameraLayout";

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private FragmentCameraLayoutBinding binding;

    private ImageCapture imageCapture;

    private Bitmap imageBitmap;

    private ActivityResultLauncher<String> imgGetContent;

    private DetectionService detectionService;

    private DetectionFragmentViewModel dfViewModel;

    NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getContext() != null) {
            cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        } else {
            Log.e(TAG, "el contexto es nulo en la creación");
        }

        imgGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if(uri!=null){
                        try{
                            InputStream inputStream = requireContext().getContentResolver()
                                    .openInputStream(uri);

                            imageBitmap = BitmapFactory.decodeStream(inputStream);

                            sendImageToDetection(resizeBitmap(imageBitmap));

                            if(inputStream!=null){
                                inputStream.close();
                            }

                        } catch (IOException e) {
                            mostrarDialogo("No se pudo cargar la imagen");
                            Log.e(TAG, e.toString());
                        }
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCameraLayoutBinding.inflate(inflater, container, false);

        dfViewModel = new ViewModelProvider(this).
                get(DetectionFragmentViewModel.class);

        navController = NavHostFragment.findNavController(this);

        setUpCamera();

        binding.imageCaptureButton.setOnClickListener(v -> {
            takeImage();
        });

        binding.searchImageButton.setOnClickListener(v -> {
            imgGetContent.launch("image/*");
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                if (binding == null || binding.photoPreview == null) {
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
                CameraInfo cameraInfo = camera.getCameraInfo();
                preview.setSurfaceProvider(binding.photoPreview.getSurfaceProvider());

                Log.d(TAG, "Cámara configurada correctamente: " + cameraInfo.toString());
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error al obtener cameraProvider", e);
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    //toma la foto desde el image capture
    public void takeImage(){
        Handler handler = new Handler(Looper.getMainLooper());
        imageCapture.takePicture(Executors.newSingleThreadExecutor(), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                super.onCaptureSuccess(image);

                Bitmap bitmap = imageToBitmap(image);
                byte[] improveBitmapByteArray = compressBitmap(bitmap);
                System.out.println("Bytes de la primera conversion desde el improveBitmap = " + improveBitmapByteArray.length);
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
                            try{
                                NavDirections action =
                                        CameraLayoutDirections.actionCameraLayout2ToDetectionFragment(resultado,
                                                ImageCacheManager.saveImageToCache(requireContext(), improveBitmapByteArray));

                                navController.navigate(action);
                            } catch (IOException e){
                                mostrarDialogo(e.getMessage());
                            }
                    });
                }
            }
        });
    }

    //toma la foto desde los archivos
    public void sendImageToDetection(Bitmap image){
        Handler handler = new Handler(Looper.getMainLooper());
        detectionService = new DetectionService();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // se crea el tensor con el metodo en detection service
                TensorImage tensor = detectionService.bitmatToTensor(image);

                // se captura el resultado y se envia a el mostrar dialogo
                final String resultado = detectionService.processDetection(tensor, getContext());
                byte[] imgBytes = compressBitmap(image);

                System.out.println("numero de buyes desde la conversion de un archivo: " + imgBytes.length);

                if(Objects.equals(resultado, "")){
                    handler.post(() -> {
                        mostrarDialogo("No se encontro resultado");
                    });
                } else {
                    handler.post(() -> {
                        try{
                            NavDirections action =
                                    CameraLayoutDirections.actionCameraLayout2ToDetectionFragment(resultado,
                                            ImageCacheManager.saveImageToCache(requireContext(), imgBytes));

                            navController.navigate(action);
                        } catch (IOException e){
                            mostrarDialogo(e.getMessage());
                        }
                    });
                }
            }
        });

        thread.start();
    }

    // metodos auxiliares

    private Bitmap imageToBitmap(ImageProxy image){
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();

        byte[] jpegBytes = new byte[buffer.remaining()];
        buffer.get(jpegBytes);

        // decodificación del arreglo de bytes
        Bitmap bitmap = BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.length);

        // se usa una matriz de graficos para corregir la rotación de la imagen
        Matrix matrix = new Matrix();
        matrix.postRotate(image.getImageInfo().getRotationDegrees());

        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    private Bitmap resizeBitmap(Bitmap bm){
        return Bitmap.createScaledBitmap(bm, 224, 224, true);
    }

    private byte[] compressBitmap(Bitmap bm){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 85, outputStream);

        return outputStream.toByteArray();
    }

    private void mostrarDialogo(String mensaje) {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Resultado de diagnostico")
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

}
