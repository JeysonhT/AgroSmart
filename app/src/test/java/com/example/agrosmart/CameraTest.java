package com.example.agrosmart;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageInfo;
import androidx.camera.core.ImageProxy;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.agrosmart.data.local.dto.MMLResultDTO;
import com.example.agrosmart.data.local.ml.DetectionService;
import com.example.agrosmart.presentation.ui.fragment.CameraLayout;
import com.example.agrosmart.presentation.viewmodels.FakeDetectionViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.versioning.AndroidVersions;
import org.tensorflow.lite.support.image.TensorImage;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

@org.robolectric.annotation.Config(sdk = 36)
@RunWith(RobolectricTestRunner.class)
public class CameraTest {
    @Test
    public void testCameraLayoutUIAndSendImage() {

        FragmentFactory factory = new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                CameraLayout fragment = new CameraLayout();
                fragment.setViewModel(new FakeDetectionViewModel());
                fragment.setNavController(mock(NavController.class));
                fragment.setIsTest(true);
                return fragment;
            }
        };

        FragmentScenario<CameraLayout> scenario = FragmentScenario.launchInContainer(
                CameraLayout.class,
                null,
                R.style.Theme_AgroSmart,
                factory
        );

        scenario.onFragment(fragment -> {
            // Verificar que los botones principales existen
            assertNotNull(fragment.binding.imageCaptureButton);
            assertNotNull(fragment.binding.searchImageButton);

            // Crear un Bitmap fake
            Bitmap fakeBitmap = Bitmap.createBitmap(224, 224, Config.ARGB_8888);


            fragment.sendImageToDetection(fakeBitmap);

        });
    }

    @Test
    public void testCameraLayoutUIAndSearchImageButton() throws InterruptedException {
        FakeDetectionViewModel realViewModel = new FakeDetectionViewModel();
        FakeDetectionViewModel spyViewModel = spy(realViewModel);
        NavController mockNavController = mock(NavController.class);

        FragmentFactory factory = new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                CameraLayout fragment = new CameraLayout();
                fragment.setViewModel(spyViewModel);
                fragment.setNavController(mockNavController);
                fragment.setIsTest(true);
                return fragment;
            }
        };

        FragmentScenario<CameraLayout> scenario = FragmentScenario.launchInContainer(
                CameraLayout.class,
                null,
                R.style.Theme_AgroSmart,
                factory
        );

        scenario.onFragment(fragment -> {
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
            assertNotNull(fragment.binding.searchImageButton);

            Bitmap fakeBitmap = Bitmap.createBitmap(224, 224, Bitmap.Config.ARGB_8888);

            // Spy del viewModel para detectar cuando processDetection se llame
            doAnswer(invocation -> {
                Object result = invocation.callRealMethod(); // sigue ejecutando el método real
                // libera el latch cuando processDetection termina
                ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
                return result;
            }).when(spyViewModel).processDetection(any(), any());

            fragment.sendImageToDetection(fakeBitmap);

            // Ejecutar cualquier Handler.post pendiente
            shadowOf(Looper.getMainLooper()).idle();

            // Ahora sí podemos verificar interacciones
            verify(spyViewModel, atLeastOnce()).sendResulToFirebase(any());
            verify(spyViewModel, atLeastOnce()).sendStatsToFirebase(any());
            verify(mockNavController, atLeastOnce()).navigate(any(NavDirections.class));
        });
    }


    @Test
    public void testImageCaptureButtonWithFragmentFactory() {
        // Fake ViewModel real
        FakeDetectionViewModel fakeViewModel = new FakeDetectionViewModel();
        NavController mockNavController = mock(NavController.class);

        // FragmentFactory personalizado para inyectar dependencias
        FragmentFactory fragmentFactory = new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                CameraLayout fragment = (CameraLayout) super.instantiate(classLoader, className);
                fragment.setViewModel(fakeViewModel);
                fragment.setNavController(mockNavController);
                fragment.setIsTest(true); // Si tu fragment tiene este flag para pruebas
                return fragment;
            }
        };

        // Lanzar el fragment
        FragmentScenario<CameraLayout> scenario = FragmentScenario.launchInContainer(
                CameraLayout.class,
                null,
                R.style.Theme_AgroSmart,
                fragmentFactory
        );

        scenario.onFragment(fragment -> {
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

            // Verificar que los botones existen
            assertNotNull(fragment.binding.imageCaptureButton);
            assertNotNull(fragment.binding.searchImageButton);

            // Mock ImageCapture para que no use la cámara real
            ImageCapture mockImageCapture = mock(ImageCapture.class);
            fragment.imageCapture = mockImageCapture;

            // Configurar la captura simulada
            doAnswer(invocation -> {
                ImageCapture.OnImageCapturedCallback callback = invocation.getArgument(1);

                // Crear ImageProxy simulado
                ImageProxy mockImage = mock(ImageProxy.class);
                ImageProxy.PlaneProxy mockPlane = mock(ImageProxy.PlaneProxy.class);
                ByteBuffer fakeBuffer = ByteBuffer.allocate(224 * 224 * 4);
                when(mockPlane.getBuffer()).thenReturn(fakeBuffer);
                when(mockImage.getPlanes()).thenReturn(new ImageProxy.PlaneProxy[]{mockPlane});

                ImageInfo mockImageInfo = mock(ImageInfo.class);
                when(mockImageInfo.getRotationDegrees()).thenReturn(0);
                when(mockImage.getImageInfo()).thenReturn(mockImageInfo);

                // Llamar al callback de éxito
                callback.onCaptureSuccess(mockImage);

                ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

                return null;
            }).when(mockImageCapture).takePicture(any(), any());

            // Simular click en el botón
            fragment.binding.imageCaptureButton.performClick();

            // Verificar que el ViewModel recibió la llamada
            assertNotNull(fakeViewModel.getLastDiagnosis().getValue());

            // Verificar que el NavController fue invocado
            verify(mockNavController).navigate(any(NavDirections.class));
        });
    }

}
